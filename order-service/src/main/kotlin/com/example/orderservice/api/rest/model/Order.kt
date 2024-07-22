package com.example.orderservice.api.rest.model

import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.*

data class CreateOrderRequest(
    val userId: UUID,
    val items: Set<Item>
)

data class Order(
    val id: UUID,
    val userId: UUID,
    val status: Status,
    val items: Set<Item>,
    val totalQuantity: Int,
    val totalPrice: BigDecimal,
    val currency: Currency,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
)

data class Item(
    val id: UUID,
    val category: ItemCategory,
    val quantity: Int,
    val price: BigDecimal,
    val currency: ItemCurrency
)

enum class ItemCategory {
    BOOK
}

enum class ItemCurrency {
    RUB, EUR
}

enum class Status {
    CREATED,
    IN_PROGRESS,
    DECLINED,
    CANCELLED,
    COMPLETED
}

enum class Currency {
    RUB, EUR
}