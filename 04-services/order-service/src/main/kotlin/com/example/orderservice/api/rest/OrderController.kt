package com.example.orderservice.api.rest

import com.example.orderservice.api.rest.RequestValidation.validate
import com.example.orderservice.api.rest.model.*
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
    ): Order = orderService.createOrder(idempotencyKey, request.validate())

    @Operation(summary = "Update order status")
    @PatchMapping("/orders/{orderId}/status")
    fun updateOrderStatus(
        @PathVariable("orderId") orderId: UUID,
        @RequestBody request: UpdateOrderStatusRequest
    ): Order = orderService.updateOrderStatus(orderId, request)

}

private object RequestValidation {

    fun CreateOrderRequest.validate(): CreateOrderRequest {
        val currencies = this.items.map { it.price.currency }
        if (currencies.distinct().size == 1) return this
        else throw RequestValidationException("Item prices must have the same currency, but they are $currencies")
    }
}