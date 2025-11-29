package com.example.orderservice.domain.kafka.client.model

import com.example.online.shop.model.PriceValue
import com.example.online.shop.model.Quantity
import java.time.OffsetDateTime
import java.util.*

data class Order(
    val id: UUID,
    val userId: UUID,
    val status: Status,
    val items: Set<OrderItem>,
    val totalQuantity: Quantity,
    val totalPrice: TotalPrice,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
) : Data

data class TotalPrice(
    val value: PriceValue,
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