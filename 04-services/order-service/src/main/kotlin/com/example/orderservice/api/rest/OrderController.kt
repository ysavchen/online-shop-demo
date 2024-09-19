package com.example.orderservice.api.rest

import com.example.orderservice.api.rest.model.CreateOrderRequest
import com.example.orderservice.api.rest.model.Order
import com.example.orderservice.api.rest.model.OrderSearchRequest
import com.example.orderservice.service.OrderService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.data.web.PagedModel
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1", produces = [MediaType.APPLICATION_JSON_VALUE])
class OrderController(private val orderService: OrderService) {

    @Operation(summary = "Search orders")
    @PostMapping("/orders/search")
    fun orders(
        orderRequestParams: OrderRequestParams,
        @RequestBody request: OrderSearchRequest
    ): PagedModel<Order> = orderService.getOrders(orderRequestParams, request)

    @Operation(summary = "Get order by id")
    @GetMapping("/orders/{orderId}")
    fun orderById(@PathVariable("orderId") orderId: UUID): Order = orderService.getOrderById(orderId)

    @Operation(summary = "Create order")
    @PostMapping("/orders", consumes = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun createOrder(
        @RequestHeader(IDEMPOTENCY_KEY) idempotencyKey: UUID,
        @RequestBody request: CreateOrderRequest
    ): Order = orderService.createOrder(idempotencyKey, request)

}