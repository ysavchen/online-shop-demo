package com.example.orderservice.api.rest

import com.example.orderservice.api.rest.model.*
import com.example.orderservice.mapping.OrderItemMapper.toEntity
import com.example.orderservice.mapping.OrderMapper.toModel
import com.example.orderservice.repository.OrderItemRepository
import com.example.orderservice.repository.OrderRepository
import com.example.orderservice.repository.entity.StatusEntity
import com.example.orderservice.test.IntegrationTest
import com.example.orderservice.test.OrderTestData.createOrderRequest
import com.example.orderservice.test.OrderTestData.orderEntity
import com.example.orderservice.test.OrderTestData.orderItem
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.core.StringContains.containsString
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post
import java.util.*

@IntegrationTest
class OrderControllerTests {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var orderRepository: OrderRepository

    @Autowired
    lateinit var orderItemRepository: OrderItemRepository

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun `search orders by userId`() {
        val order = orderRepository.save(orderEntity()).toModel()
        val request = OrderSearchRequest(order.userId)

        val result = mockMvc.post("/api/v1/orders/search?page=0") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }.andReturn()

        val expectedOrder = objectMapper.writeValueAsString(order)
        assertThat(result.response.contentAsString).contains(expectedOrder)
    }

    @Test
    fun `search orders by non-existing userId`() {
        val request = OrderSearchRequest(UUID.randomUUID())

        mockMvc.post("/api/v1/orders/search?page=0") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
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

        mockMvc.post("/api/v1/orders/search?page=0&sortBy=invalidSortBy") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
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

        mockMvc.post("/api/v1/orders/search?page=0&orderBy=invalidOrderBy") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
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

        val expectedOrder = objectMapper.writeValueAsString(order)
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

    @Disabled("Mock for BookServiceClient is needed")
    @Test
    fun `create order`() {
        val request = createOrderRequest()
        val idempotencyKey = UUID.randomUUID()

        val result = mockMvc.post("/api/v1/orders") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            header(IDEMPOTENCY_KEY, idempotencyKey)
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isCreated() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }.andReturn()

        val createdOrder = objectMapper.readValue(result.response.contentAsString, Order::class.java)
        assertThat(createdOrder)
            .hasFieldOrProperty("id")
            .hasFieldOrProperty("createdAt")
            .hasFieldOrProperty("updatedAt")
            .hasFieldOrPropertyWithValue("userId", request.userId)
            .hasFieldOrPropertyWithValue("status", Status.CREATED)
            .hasFieldOrPropertyWithValue("items", request.items)
            .hasFieldOrPropertyWithValue("totalQuantity", request.items.size)
            .extracting(
                { it.totalPrice.value },
                { it.totalPrice.currency.name }
            ).containsExactly(
                request.items.sumOf { it.price.value },
                request.items.first().price.currency.name
            )
    }

    @Disabled("Mock for BookServiceClient is needed")
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
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isBadRequest() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { string(containsString(ErrorCode.REQUEST_VALIDATION_ERROR.name)) }
        }
    }

    @Disabled("Mock for BookServiceClient is needed")
    @Test
    fun `duplicate create order request`() {
        val request = createOrderRequest()
        val idempotencyKey = UUID.randomUUID()

        mockMvc.post("/api/v1/orders") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            header(IDEMPOTENCY_KEY, idempotencyKey)
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isCreated() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }

        mockMvc.post("/api/v1/orders") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            header(IDEMPOTENCY_KEY, idempotencyKey)
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isConflict() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { string(containsString(ErrorCode.REQUEST_ALREADY_PROCESSED.name)) }
        }
    }

    @Test
    fun `update order to new status`() {
        val order = orderRepository.save(orderEntity(status = StatusEntity.CREATED)).toModel()
        val request = UpdateOrderStatusRequest(status = Status.IN_PROGRESS)

        val result = mockMvc.patch("/api/v1/orders/${order.id}/status") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }.andReturn()

        val updatedOrder = objectMapper.readValue(result.response.contentAsString, Order::class.java)
        assertThat(updatedOrder)
            .hasFieldOrPropertyWithValue("id", order.id)
            .hasFieldOrPropertyWithValue("status", Status.IN_PROGRESS)
    }

    @Test
    fun `update order to the same status`() {
        val order = orderRepository.save(orderEntity(status = StatusEntity.IN_PROGRESS)).toModel()
        val request = UpdateOrderStatusRequest(status = Status.IN_PROGRESS)

        val result = mockMvc.patch("/api/v1/orders/${order.id}/status") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }.andReturn()

        val updatedOrder = objectMapper.readValue(result.response.contentAsString, Order::class.java)
        assertThat(updatedOrder)
            .hasFieldOrPropertyWithValue("id", order.id)
            .hasFieldOrPropertyWithValue("status", Status.IN_PROGRESS)
    }

    @Test
    fun `update order to invalid status`() {
        val order = orderRepository.save(orderEntity(status = StatusEntity.CREATED)).toModel()
        val request = UpdateOrderStatusRequest(status = Status.DELIVERED)

        mockMvc.patch("/api/v1/orders/${order.id}/status") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isForbidden() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { string(containsString(ErrorCode.INVALID_ORDER_STATUS_UPDATE.name)) }
        }.andReturn()
    }

    @Test
    fun `update non-existing order to new status`() {
        val request = UpdateOrderStatusRequest(status = Status.IN_PROGRESS)

        mockMvc.patch("/api/v1/orders/${UUID.randomUUID()}/status") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isNotFound() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { string(containsString(ErrorCode.RESOURCE_NOT_FOUND.name)) }
        }.andReturn()
    }
}