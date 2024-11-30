package com.example.orderservice.service

import com.example.deliveryservice.kafka.client.model.DeliveryCreatedResponse
import com.example.deliveryservice.kafka.client.model.ResponseDeliveryMessage
import com.example.orderservice.api.rest.DuplicateRequestException
import com.example.orderservice.api.rest.InvalidOrderStatusUpdate
import com.example.orderservice.api.rest.OrderNotFoundException
import com.example.orderservice.api.rest.RequestValidationException
import com.example.orderservice.api.rest.model.*
import com.example.orderservice.config.CacheConfiguration.Companion.ORDER_CACHE_NAME
import com.example.orderservice.domain.kafka.client.model.OrderCreatedEvent
import com.example.orderservice.domain.kafka.client.model.OrderUpdatedEvent
import com.example.orderservice.mapping.api.OrderMapper.toEntity
import com.example.orderservice.mapping.api.OrderMapper.toModel
import com.example.orderservice.mapping.api.OrderMapper.toPagedModel
import com.example.orderservice.mapping.api.RequestMapper.toPageable
import com.example.orderservice.mapping.integration.OrderMapper.toDomainModel
import com.example.orderservice.repository.IdempotencyKeyRepository
import com.example.orderservice.repository.OrderRepository
import com.example.orderservice.repository.OrderRepository.Companion.searchSpec
import com.example.orderservice.repository.entity.IdempotencyKeyEntity
import com.example.orderservice.repository.entity.ResourceEntity
import com.example.orderservice.repository.entity.StatusEntity
import com.example.orderservice.repository.entity.StatusEntity.*
import com.example.orderservice.service.RequestValidation.validate
import com.example.orderservice.service.integration.BookClientService
import com.example.orderservice.service.integration.DomainEventService
import jakarta.persistence.OptimisticLockException
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.data.web.PagedModel
import org.springframework.retry.support.RetryTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionTemplate
import java.util.*

@Service
@CacheConfig(cacheNames = [ORDER_CACHE_NAME])
class OrderService(
    private val metricService: MetricService,
    private val orderRepository: OrderRepository,
    private val idempotencyKeyRepository: IdempotencyKeyRepository,
    private val transactionManager: PlatformTransactionManager,
    private val cacheService: CacheService,
    private val retryTemplate: RetryTemplate,
    private val bookClientService: BookClientService,
    private val domainEventService: DomainEventService
) {

    private val transactionTemplate = TransactionTemplate(transactionManager)

    @Transactional(readOnly = true)
    fun getOrders(orderRequestParams: OrderRequestParams, request: OrderSearchRequest): PagedModel<Order> =
        orderRepository.findAll(searchSpec(request), orderRequestParams.toPageable()).toPagedModel()

    @Cacheable(key = "#orderId")
    @Transactional(readOnly = true)
    fun getOrderById(orderId: UUID): Order = orderRepository.findByIdOrNull(orderId)?.toModel()
        ?: throw OrderNotFoundException(orderId)

    @CachePut(key = "#result.id")
    fun createOrder(idempotencyKey: UUID, request: CreateOrderRequest): Order {
        request.validate()
        transactionTemplate.execute {
            val key = idempotencyKeyRepository.findByIdOrNull(idempotencyKey)
            if (key?.resourceId != null) {
                throw DuplicateRequestException(key.idempotencyKey, key.resourceId, key.resource.name.lowercase())
            }
        }
        bookClientService.validateBooks(request.items)

        val order = transactionTemplate.execute {
            val savedOrder = orderRepository.save(request.toEntity())
            idempotencyKeyRepository.save(
                IdempotencyKeyEntity(idempotencyKey, savedOrder.id!!, ResourceEntity.ORDER)
            )
            savedOrder.toModel()
        }!!.also { order ->
            metricService.countOrders(order.status)
            metricService.lastOrderTime(order.createdAt)
            metricService.orderPriceSummary(order.totalPrice)
        }.also { order ->
            val event = OrderCreatedEvent(order.toDomainModel())
            domainEventService.send(event)
        }
        return order
    }

    fun updateOrderStatus(orderId: UUID, newStatus: Status): Order {
        val order = retryTemplate.execute<Order, OptimisticLockException> {
            transactionTemplate.execute {
                val orderEntity = orderRepository.findByIdOrNull(orderId) ?: throw OrderNotFoundException(orderId)
                val newStatusEntity = newStatus.toEntity()
                if (isStatusUpdateValid(orderEntity.status, newStatusEntity)) {
                    orderEntity.status = newStatusEntity
                } else {
                    throw InvalidOrderStatusUpdate(orderEntity.id!!, orderEntity.status.toModel(), newStatus)
                }
                orderEntity.toModel()
            }
        }.also { order ->
            if (order.status != newStatus) {
                metricService.countOrders(order.status)
            }
        }.also { order ->
            val event = OrderUpdatedEvent(order.toDomainModel())
            domainEventService.send(event)
        }.also { order ->
            cacheService.set(order)
        }
        return order
    }

    fun processMessage(message: ConsumerRecord<UUID, ResponseDeliveryMessage>) {
        when (val response = message.value()) {
            is DeliveryCreatedResponse -> updateOrderStatus(response.data.orderId, Status.IN_PROGRESS)
        }
    }

    private fun isStatusUpdateValid(currentStatus: StatusEntity, newStatus: StatusEntity): Boolean =
        if (currentStatus == newStatus) true
        else if (currentStatus == CREATED && newStatus in listOf(IN_PROGRESS, DECLINED, CANCELLED)) true
        else if (currentStatus == IN_PROGRESS && newStatus in listOf(DECLINED, CANCELLED, DELIVERED)) true
        else false

}

private object RequestValidation {

    fun CreateOrderRequest.validate(): CreateOrderRequest {
        val currencies = this.items.map { it.price.currency }
        if (currencies.distinct().size == 1) return this
        else throw RequestValidationException("Item prices must have the same currency, but they are $currencies")
    }
}