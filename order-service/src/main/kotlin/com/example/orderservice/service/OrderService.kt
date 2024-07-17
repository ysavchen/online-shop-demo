package com.example.orderservice.service

import com.example.orderservice.api.rest.model.Order
import com.example.orderservice.api.rest.error.OrderNotFoundException
import com.example.orderservice.api.rest.model.OrderRequestParams
import com.example.orderservice.mapping.OrderMapper.toModel
import com.example.orderservice.mapping.OrderMapper.toPagedModel
import com.example.orderservice.mapping.RequestMapper.toPageable
import com.example.orderservice.repository.OrderRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.data.web.PagedModel
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class OrderService(private val orderRepository: OrderRepository) {

    @Transactional(readOnly = true)
    fun getOrders(orderRequestParams: OrderRequestParams): PagedModel<Order> =
        orderRepository.findAll(orderRequestParams.toPageable()).toPagedModel()

    @Transactional(readOnly = true)
    fun getOrderById(orderId: UUID): Order = orderRepository.findByIdOrNull(orderId)?.toModel()
        ?: throw OrderNotFoundException(orderId)

}