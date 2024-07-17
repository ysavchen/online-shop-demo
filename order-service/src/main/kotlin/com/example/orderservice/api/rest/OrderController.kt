package com.example.orderservice.api.rest

import com.example.orderservice.api.rest.RestCompanion.BASE_PATH_V1
import com.example.orderservice.api.rest.model.Order
import com.example.orderservice.api.rest.model.OrderRequestParams
import com.example.orderservice.service.OrderService
import org.springframework.data.web.PagedModel
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping(BASE_PATH_V1, produces = [MediaType.APPLICATION_JSON_VALUE])
class OrderController(private val orderService: OrderService) {

    @PostMapping("/orders/search")
    fun orders(orderRequestParams: OrderRequestParams): PagedModel<Order> =
        orderService.getOrders(orderRequestParams)

    @GetMapping("/orders/{orderId}")
    fun orderById(@PathVariable("orderId") orderId: UUID): Order = orderService.getOrderById(orderId)

}