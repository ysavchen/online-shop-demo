package com.example.orderservice.api.rest.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonFormat.Shape
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.*

data class OrderSearchRequest(
    val userId: UUID
)

data class CreateOrderRequest(
    val userId: UUID,
    val items: Set<Item>
)

data class UpdateOrderStatusRequest(
    val status: Status
)

data class Order(
    val id: UUID,
    val userId: UUID,
    val status: Status,
    val items: Set<Item>,
    val totalQuantity: Int,
    val totalPrice: TotalPrice,
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    val createdAt: OffsetDateTime,
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    val updatedAt: OffsetDateTime
)

data class Item(
    val id: UUID,
    val category: ItemCategory,
    val quantity: Int,
    val price: ItemPrice
)

data class ItemPrice(
    val value: BigDecimal,
    val currency: ItemCurrency
)

data class TotalPrice(
    val value: BigDecimal,
    val currency: Currency
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