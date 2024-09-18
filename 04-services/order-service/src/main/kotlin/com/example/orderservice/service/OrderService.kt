package com.example.orderservice.service

import com.example.orderservice.api.rest.DuplicateRequestException
import com.example.orderservice.api.rest.OrderNotFoundException
import com.example.orderservice.api.rest.OrderRequestParams
import com.example.orderservice.api.rest.model.CreateOrderRequest
import com.example.orderservice.api.rest.model.Order
import com.example.orderservice.api.rest.model.OrderSearchRequest
import com.example.orderservice.mapping.OrderMapper.toEntity
import com.example.orderservice.mapping.OrderMapper.toModel
import com.example.orderservice.mapping.OrderMapper.toPagedModel
import com.example.orderservice.mapping.RequestMapper.toPageable
import com.example.orderservice.repository.IdempotencyKeyRepository
import com.example.orderservice.repository.OrderRepository
import com.example.orderservice.repository.OrderRepository.Companion.searchSpec
import com.example.orderservice.repository.entity.IdempotencyKeyEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.data.web.PagedModel
import org.springframework.stereotype.Service
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionTemplate
import java.util.*

@Service
class OrderService(
    private val metricService: MetricService,
    private val orderRepository: OrderRepository,
    private val idempotencyKeyRepository: IdempotencyKeyRepository,
    private val transactionManager: PlatformTransactionManager
) {

    private val transactionTemplate = TransactionTemplate(transactionManager)

    @Transactional(readOnly = true)
    fun getOrders(orderRequestParams: OrderRequestParams, request: OrderSearchRequest): PagedModel<Order> =
        orderRepository.findAll(searchSpec(request), orderRequestParams.toPageable()).toPagedModel()

    @Transactional(readOnly = true)
    fun getOrderById(orderId: UUID): Order = orderRepository.findByIdOrNull(orderId)?.toModel()
        ?: throw OrderNotFoundException(orderId)

    fun createOrder(idempotencyKey: UUID, request: CreateOrderRequest): Order {
        val order = transactionTemplate.execute {
            val key = idempotencyKeyRepository.findByIdOrNull(idempotencyKey)
            if (key?.orderId != null) throw DuplicateRequestException(key.idempotencyKey, key.orderId)
            val savedOrder = orderRepository.save(request.toEntity())
            idempotencyKeyRepository.save(IdempotencyKeyEntity(idempotencyKey, savedOrder.id!!))
            savedOrder.toModel()
        }
        metricService.countOrders(order!!.status)
        return order
    }

}