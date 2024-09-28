package com.example.orderservice.mapping

import com.example.orderservice.api.rest.model.ItemCategory
import com.example.orderservice.api.rest.model.ItemCurrency
import com.example.orderservice.api.rest.model.ItemPrice
import com.example.orderservice.api.rest.model.OrderItem
import com.example.orderservice.repository.entity.ItemCategoryEntity
import com.example.orderservice.repository.entity.ItemCurrencyEntity
import com.example.orderservice.repository.entity.ItemPriceEntity
import com.example.orderservice.repository.entity.OrderItemEntity

object OrderItemMapper {

    fun OrderItemEntity.toModel() = OrderItem(
        id = id,
        category = category.toModel(),
        price = ItemPrice(
            value = price.value,
            currency = price.currency.toModel()
        ),
        quantity = quantity
    )

    fun OrderItem.toEntity() = OrderItemEntity(
        id = id,
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