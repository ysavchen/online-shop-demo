package com.example.orderservice.api.rest.model

import com.example.online.shop.model.PriceValue
import com.example.online.shop.model.Quantity
import java.io.Serializable
import java.util.*

data class OrderItem(
    val id: UUID,
    val category: ItemCategory,
    val quantity: Quantity,
    val price: ItemPrice
) : Serializable

enum class ItemCategory {
    BOOKS
}

enum class ItemCurrency {
    RUB, EUR
}

data class ItemPrice(
    val value: PriceValue,
    val currency: ItemCurrency
) : Serializable