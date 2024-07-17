package com.example.orderservice.mapping

import com.example.orderservice.api.rest.model.*
import com.example.orderservice.repository.*
import org.springframework.data.domain.Page
import org.springframework.data.web.PagedModel

object OrderMapper {

    internal fun OrderEntity.toModel() = Order(
        id = id!!,
        items = items.map { it.toModel() }.toSet(),
        status = status.toModel(),
        userId = userId,
        totalQuantity = totalQuantity,
        totalPrice = totalPrice,
        currency = currency.toModel(),
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    private fun ItemEntity.toModel() = Item(
        id = id,
        category = category.toModel(),
        price = price,
        currency = currency.toModel(),
        quantity = quantity
    )

    internal fun Page<OrderEntity>.toPagedModel() = PagedModel(
        this.map { it.toModel() }
    )

    private fun ItemCategoryEntity.toModel() = when (this) {
        ItemCategoryEntity.BOOK -> ItemCategory.BOOK
    }

    private fun ItemCurrencyEntity.toModel() = when (this) {
        ItemCurrencyEntity.RUB -> ItemCurrency.RUB
        ItemCurrencyEntity.EUR -> ItemCurrency.EUR
    }

    private fun StatusEntity.toModel() = when (this) {
        StatusEntity.CREATED -> Status.CREATED
        StatusEntity.IN_PROGRESS -> Status.IN_PROGRESS
        StatusEntity.DECLINED -> Status.DECLINED
        StatusEntity.CANCELLED -> Status.CANCELLED
        StatusEntity.COMPLETED -> Status.COMPLETED
    }

    private fun CurrencyEntity.toModel() = when (this) {
        CurrencyEntity.RUB -> Currency.RUB
        CurrencyEntity.EUR -> Currency.EUR
    }

}