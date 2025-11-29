package com.example.orderservice.domain.kafka.client.model

import com.example.online.shop.model.PriceValue
import com.example.online.shop.model.Quantity
import java.util.*

data class OrderItem(
    val id: UUID,
    val category: ItemCategory,
    val quantity: Quantity,
    val price: ItemPrice
)

enum class ItemCategory {
    BOOKS
}

enum class ItemCurrency {
    RUB, EUR
}

data class ItemPrice(
    val value: PriceValue,
    val currency: ItemCurrency
)