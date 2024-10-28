package com.example.orderservice.mapping

import com.example.orderservice.api.rest.model.*
import com.example.orderservice.repository.entity.*
import java.util.*

object OrderItemMapper {

    internal fun OrderItemEntity.toModel() = OrderItem(
        id = orderItemId.id,
        category = category.toModel(),
        price = ItemPrice(
            value = price.value,
            currency = price.currency.toModel()
        ),
        quantity = quantity
    )

    internal fun OrderItem.toEntity(orderId: UUID) = OrderItemEntity(
        orderItemId = OrderItemId(id, orderId),
        category = category.toEntity(),
        price = ItemPriceEntity(
            value = price.value,
            currency = price.currency.toEntity()
        ),
        quantity = quantity
    )

    private fun ItemCategoryEntity.toModel() = when (this) {
        ItemCategoryEntity.BOOKS -> ItemCategory.BOOKS
    }

    private fun ItemCurrencyEntity.toModel() = when (this) {
        ItemCurrencyEntity.RUB -> ItemCurrency.RUB
        ItemCurrencyEntity.EUR -> ItemCurrency.EUR
    }

    private fun ItemCategory.toEntity() = when (this) {
        ItemCategory.BOOKS -> ItemCategoryEntity.BOOKS
    }

    private fun ItemCurrency.toEntity() = when (this) {
        ItemCurrency.RUB -> ItemCurrencyEntity.RUB
        ItemCurrency.EUR -> ItemCurrencyEntity.EUR
    }
}