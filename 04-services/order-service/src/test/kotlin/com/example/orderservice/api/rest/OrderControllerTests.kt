package com.example.orderservice.api.rest

import com.example.bookservice.rest.client.BookServiceRestClient
import com.example.orderservice.api.rest.model.*
import com.example.orderservice.domain.kafka.client.model.DomainEvent
import com.example.orderservice.mapping.api.OrderMapper.toModel
import com.example.orderservice.repository.OrderRepository
import com.example.orderservice.repository.entity.StatusEntity
import com.example.orderservice.service.integration.DeliveryClientService
import com.example.orderservice.service.integration.DomainEventService
import com.example.orderservice.test.BookTestData.book
import com.example.orderservice.test.DeliveryTestData.delivery
import com.example.orderservice.test.IntegrationTest
import com.example.orderservice.test.OrderTestData.createOrderRequest
import com.example.orderservice.test.OrderTestData.orderEntity
import com.example.orderservice.test.OrderTestData.orderItem
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.core.StringContains.containsString
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.verify
import org.mockito.kotlin.wheneverBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post
import tools.jackson.databind.json.JsonMapper
import java.util.*

@Disabled
@IntegrationTest
class OrderControllerTests(
    @Autowired val mockMvc: MockMvc,
    @Autowired val jsonMapper: JsonMapper,
    @Autowired val orderRepository: OrderRepository
) {

    @MockitoBean
    lateinit var bookServiceRestClient: BookServiceRestClient

    @MockitoBean
    lateinit var domainEventService: DomainEventService

    @MockitoBean
    lateinit var deliveryClientService: DeliveryClientService

    @BeforeEach
    fun beforeEach() {
        orderRepository.deleteAll()
    }

    @Test
    fun `search orders by userId`() {
        val order = orderRepository.save(orderEntity()).toModel()
        val request = OrderSearchRequest(order.userId)

        val result = mockMvc.post("/api/v1/orders/search?page=0") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = jsonMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }.andReturn()

        val expectedOrder = jsonMapper.writeValueAsString(order)
        assertThat(result.response.contentAsString).contains(expectedOrder)
    }

    @Test
    fun `search orders by non-existing userId`() {
        val request = OrderSearchRequest(UUID.randomUUID())

        mockMvc.post("/api/v1/orders/search?page=0") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = jsonMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { string(containsString(""""content":[]""")) }
        }
    }

    @Test
    fun `search orders with non-supported sortBy parameter`() {
        val order = orderRepository.save(orderEntity()).toModel()
        val request = OrderSearchRequest(order.userId)

        mockMvc.post("/api/v1/orders/search?page=0&sort_by=invalidSortBy") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = jsonMapper.writeValueAsString(request)
        }.andExpect {
            status { isBadRequest() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { string(containsString(ErrorCode.REQUEST_VALIDATION_ERROR.name)) }
        }
    }

    @Test
    fun `search orders with non-supported orderBy parameter`() {
        val order = orderRepository.save(orderEntity()).toModel()
        val request = OrderSearchRequest(order.userId)

        mockMvc.post("/api/v1/orders/search?page=0&order_by=invalidOrderBy") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = jsonMapper.writeValueAsString(request)
        }.andExpect {
            status { isBadRequest() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { string(containsString(ErrorCode.REQUEST_VALIDATION_ERROR.name)) }
        }
    }

    @Test
    fun `get order by id`() {
        val order = orderRepository.save(orderEntity()).toModel()

        val result = mockMvc.get("/api/v1/orders/${order.id}") {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }.andReturn()

        val expectedOrder = jsonMapper.writeValueAsString(order)
        assertThat(result.response.contentAsString).contains(expectedOrder)
    }

    @Test
    fun `get order by non-existing id`() {
        mockMvc.get("/api/v1/orders/${UUID.randomUUID()}") {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isNotFound() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { string(containsString(ErrorCode.RESOURCE_NOT_FOUND.name)) }
        }
    }

    @Test
    fun `create order`() {
        val book = book()
        val request = createOrderRequest(items = setOf(orderItem(book)))
        val idempotencyKey = UUID.randomUUID()

        wheneverBlocking { bookServiceRestClient.getBookById(any<UUID>()) }.doReturn(book)
        wheneverBlocking {
            deliveryClientService.createDelivery(any<UUID>(), any<DeliveryRequest>())
        }.doReturn(delivery())

        val result = mockMvc.post("/api/v1/orders") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            header(IDEMPOTENCY_KEY, idempotencyKey)
            content = jsonMapper.writeValueAsString(request)
        }.andExpect {
            status { isCreated() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }.andReturn()

        val createdOrder = jsonMapper.readValue(result.response.contentAsString, Order::class.java)
        assertThat(createdOrder)
            .hasFieldOrProperty("id")
            .hasFieldOrProperty("createdAt")
            .hasFieldOrProperty("updatedAt")
            .hasFieldOrPropertyWithValue("userId", request.userId)
            .hasFieldOrPropertyWithValue("status", Status.CREATED)
            .hasFieldOrPropertyWithValue("items", request.items)
            .hasFieldOrPropertyWithValue("totalQuantity", request.items.sumOf { it.quantity.value })
            .extracting(
                { it.totalPrice.value.value },
                { it.totalPrice.currency.name }
            ).containsExactly(
                request.items.sumOf { it.price.value.value },
                request.items.first().price.currency.name
            )

        verify(domainEventService).send(any<DomainEvent>())
        verify(deliveryClientService).createDelivery(any<UUID>(), any<DeliveryRequest>())
    }

    @Test
    fun `create order with different currencies`() {
        val rubItem = orderItem(currency = ItemCurrency.RUB)
        val euroItem = orderItem(currency = ItemCurrency.EUR)
        val request = createOrderRequest(items = setOf(rubItem, euroItem))
        val idempotencyKey = UUID.randomUUID()

        mockMvc.post("/api/v1/orders") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            header(IDEMPOTENCY_KEY, idempotencyKey)
            content = jsonMapper.writeValueAsString(request)
        }.andExpect {
            status { isBadRequest() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { string(containsString(ErrorCode.REQUEST_VALIDATION_ERROR.name)) }
        }
    }

    @Test
    fun `duplicate create order request`() {
        val book = book()
        val request = createOrderRequest(items = setOf(orderItem(book)))
        val idempotencyKey = UUID.randomUUID()

        wheneverBlocking { bookServiceRestClient.getBookById(any<UUID>()) }.doReturn(book)
        wheneverBlocking {
            deliveryClientService.createDelivery(any<UUID>(), any<DeliveryRequest>())
        }.doReturn(delivery())

        mockMvc.post("/api/v1/orders") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            header(IDEMPOTENCY_KEY, idempotencyKey)
            content = jsonMapper.writeValueAsString(request)
        }.andExpect {
            status { isCreated() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }

        mockMvc.post("/api/v1/orders") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            header(IDEMPOTENCY_KEY, idempotencyKey)
            content = jsonMapper.writeValueAsString(request)
        }.andExpect {
            status { isConflict() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { string(containsString(ErrorCode.REQUEST_ALREADY_PROCESSED.name)) }
        }

        verify(domainEventService).send(any<DomainEvent>())
        verify(deliveryClientService).createDelivery(any<UUID>(), any<DeliveryRequest>())
    }

    @Test
    fun `update order to new status`() {
        val order = orderRepository.save(orderEntity(status = StatusEntity.CREATED)).toModel()
        val request = UpdateOrderStatusRequest(status = Status.IN_PROGRESS)

        val result = mockMvc.patch("/api/v1/orders/${order.id}/status") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = jsonMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }.andReturn()

        val updatedOrder = jsonMapper.readValue(result.response.contentAsString, Order::class.java)
        assertThat(updatedOrder)
            .hasFieldOrPropertyWithValue("id", order.id)
            .hasFieldOrPropertyWithValue("status", Status.IN_PROGRESS)

        verify(domainEventService).send(any<DomainEvent>())
    }

    @Test
    fun `update order to the same status`() {
        val order = orderRepository.save(orderEntity(status = StatusEntity.IN_PROGRESS)).toModel()
        val request = UpdateOrderStatusRequest(status = Status.IN_PROGRESS)

        val result = mockMvc.patch("/api/v1/orders/${order.id}/status") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = jsonMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }.andReturn()

        val updatedOrder = jsonMapper.readValue(result.response.contentAsString, Order::class.java)
        assertThat(updatedOrder)
            .hasFieldOrPropertyWithValue("id", order.id)
            .hasFieldOrPropertyWithValue("status", Status.IN_PROGRESS)

        verify(domainEventService).send(any<DomainEvent>())
    }

    @Test
    fun `update order to invalid status`() {
        val order = orderRepository.save(orderEntity(status = StatusEntity.CREATED)).toModel()
        val request = UpdateOrderStatusRequest(status = Status.DELIVERED)

        mockMvc.patch("/api/v1/orders/${order.id}/status") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = jsonMapper.writeValueAsString(request)
        }.andExpect {
            status { isForbidden() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { string(containsString(ErrorCode.INVALID_ORDER_STATUS_UPDATE.name)) }
        }
    }

    @Test
    fun `update non-existing order to new status`() {
        val request = UpdateOrderStatusRequest(status = Status.IN_PROGRESS)

        mockMvc.patch("/api/v1/orders/${UUID.randomUUID()}/status") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = jsonMapper.writeValueAsString(request)
        }.andExpect {
            status { isNotFound() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { string(containsString(ErrorCode.RESOURCE_NOT_FOUND.name)) }
        }
    }
}