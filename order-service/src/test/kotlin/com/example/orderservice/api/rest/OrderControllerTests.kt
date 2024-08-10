package com.example.orderservice.api.rest

import com.example.orderservice.api.rest.model.OrderSearchRequest
import com.example.orderservice.mapping.OrderMapper.toModel
import com.example.orderservice.repository.OrderRepository
import com.example.orderservice.test.IntegrationTest
import com.example.orderservice.test.OrderTestData.orderEntity
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

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

    }

    @Test
    fun `search with non-supported sorting`() {

    }

    @Test
    fun `get order by id`() {

    }

    @Test
    fun `get order by non-existing id`() {

    }

    @Test
    fun `create order`() {

    }

    @Test
    fun `duplicate create order request`() {

    }
}