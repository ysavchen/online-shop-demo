package com.example.orderservice.api.rest.model

import java.math.BigDecimal
import java.util.*

data class OrderItem(
    val id: UUID,
    val category: ItemCategory,
    val quantity: Int,
    val price: ItemPrice
)

enum class ItemCategory {
    BOOKS
}

enum class ItemCurrency {
    RUB, EUR
}

data class ItemPrice(
    val value: BigDecimal,
    val currency: ItemCurrency
)