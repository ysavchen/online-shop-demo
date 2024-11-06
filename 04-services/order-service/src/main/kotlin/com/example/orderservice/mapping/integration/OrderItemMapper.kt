package com.example.orderservice.mapping.integration

import com.example.orderservice.api.rest.model.ItemCategory
import com.example.orderservice.api.rest.model.ItemCurrency
import com.example.orderservice.api.rest.model.OrderItem

typealias DomainItemCategory = com.example.orderservice.domain.kafka.client.model.ItemCategory
typealias DomainItemCurrency = com.example.orderservice.domain.kafka.client.model.ItemCurrency
typealias DomainItemPrice = com.example.orderservice.domain.kafka.client.model.ItemPrice
typealias DomainOrderItem = com.example.orderservice.domain.kafka.client.model.OrderItem

object OrderItemMapper {

    internal fun OrderItem.toDomainModel() = DomainOrderItem(
        id = id,
        category = category.toDomainModel(),
        price = DomainItemPrice(
            value = price.value,
            currency = price.currency.toDomainModel()
        ),
        quantity = quantity
    )

    private fun ItemCategory.toDomainModel() = when (this) {
        ItemCategory.BOOKS -> DomainItemCategory.BOOKS
    }

    private fun ItemCurrency.toDomainModel() = when (this) {
        ItemCurrency.RUB -> DomainItemCurrency.RUB
        ItemCurrency.EUR -> DomainItemCurrency.EUR
    }
}