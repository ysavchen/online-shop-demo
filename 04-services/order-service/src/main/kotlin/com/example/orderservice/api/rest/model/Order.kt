package com.example.orderservice.api.rest.model

import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.*

data class OrderSearchRequest(
    val userId: UUID
)

data class CreateOrderRequest(
    val userId: UUID,
    val items: Set<OrderItem>
)

data class UpdateOrderStatusRequest(
    val status: Status
)

data class Order(
    val id: UUID,
    val userId: UUID,
    val status: Status,
    val items: Set<OrderItem>,
    val totalQuantity: Int,
    val totalPrice: TotalPrice,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
)

data class TotalPrice(
    val value: BigDecimal,
    val currency: Currency
)

enum class Status {
    CREATED,
    IN_PROGRESS,
    DECLINED,
    CANCELLED,
    DELIVERED
}

enum class Currency {
    RUB, EUR
}