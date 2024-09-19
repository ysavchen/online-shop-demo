package com.example.orderservice.api.rest

import com.example.orderservice.api.rest.model.ErrorCode
import com.example.orderservice.api.rest.model.Order
import com.example.orderservice.api.rest.model.OrderSearchRequest
import com.example.orderservice.api.rest.model.Status
import com.example.orderservice.mapping.OrderMapper.toModel
import com.example.orderservice.repository.OrderRepository
import com.example.orderservice.test.IntegrationTest
import com.example.orderservice.test.OrderTestData.createOrderRequest
import com.example.orderservice.test.OrderTestData.orderEntity
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.core.StringContains.containsString
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.util.*

@IntegrationTest
class OrderControllerTests {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var orderRepository: OrderRepository

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
    fun `search with non-supported sorting`() {
        val order = orderRepository.save(orderEntity()).toModel()
        val request = OrderSearchRequest(order.userId)

        mockMvc.post("/api/v1/orders/search?page=0&sortBy=language") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isBadRequest() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { string(containsString(ErrorCode.SORTING_CATEGORY_NOT_SUPPORTED.name)) }
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
            .hasFieldOrPropertyWithValue("userId", request.userId)
            .hasFieldOrPropertyWithValue("status", Status.CREATED)
            .hasFieldOrPropertyWithValue("items", request.items)
            .hasFieldOrPropertyWithValue("totalQuantity", request.items.size)
            .extracting("totalPrice.value").isEqualTo(request.items.sumOf { it.price.value })
    }

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
}