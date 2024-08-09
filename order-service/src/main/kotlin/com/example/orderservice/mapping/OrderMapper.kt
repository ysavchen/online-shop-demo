package com.example.orderservice.mapping

import com.example.orderservice.api.rest.model.*
import com.example.orderservice.repository.entity.*
import org.springframework.data.domain.Page
import org.springframework.data.web.PagedModel
import java.time.OffsetDateTime

object OrderMapper {

    internal fun OrderEntity.toModel() = Order(
        id = id!!,
        userId = userId,
        status = status.toModel(),
        items = items.map { it.toModel() }.toSet(),
        totalQuantity = totalQuantity,
        totalPrice = totalPrice.toModel(),
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    internal fun ItemEntity.toModel() = Item(
        id = id,
        category = category.toModel(),
        price = price,
        currency = currency.toModel(),
        quantity = quantity
    )

    internal fun TotalPriceEntity.toModel() = TotalPrice(
        value = value,
        currency = currency.toModel()
    )

    internal fun Page<OrderEntity>.toPagedModel() = PagedModel(
        this.map { it.toModel() }
    )

    internal fun ItemCategoryEntity.toModel() = when (this) {
        ItemCategoryEntity.BOOK -> ItemCategory.BOOK
    }

    internal fun ItemCurrencyEntity.toModel() = when (this) {
        ItemCurrencyEntity.RUB -> ItemCurrency.RUB
        ItemCurrencyEntity.EUR -> ItemCurrency.EUR
    }

    internal fun StatusEntity.toModel() = when (this) {
        StatusEntity.CREATED -> Status.CREATED
        StatusEntity.IN_PROGRESS -> Status.IN_PROGRESS
        StatusEntity.DECLINED -> Status.DECLINED
        StatusEntity.CANCELLED -> Status.CANCELLED
        StatusEntity.COMPLETED -> Status.COMPLETED
    }

    internal fun CurrencyEntity.toModel() = when (this) {
        CurrencyEntity.RUB -> Currency.RUB
        CurrencyEntity.EUR -> Currency.EUR
    }

    internal fun CreateOrderRequest.toEntity() = OrderEntity(
        userId = userId,
        status = StatusEntity.CREATED,
        items = items.map { it.toEntity() }.toSet(),
        totalQuantity = items.size,
        totalPrice = TotalPriceEntity(
            value = items.sumOf { it.price },
            currency = itemsCurrencyEntity(items)
        ),
        createdAt = OffsetDateTime.now(),
        updatedAt = OffsetDateTime.now()
    )

    internal fun Item.toEntity() = ItemEntity(
        id = id,
        category = category.toEntity(),
        price = price,
        currency = currency.toEntity(),
        quantity = quantity
    )

    internal fun ItemCategory.toEntity() = when (this) {
        ItemCategory.BOOK -> ItemCategoryEntity.BOOK
    }

    internal fun ItemCurrency.toEntity() = when (this) {
        ItemCurrency.RUB -> ItemCurrencyEntity.RUB
        ItemCurrency.EUR -> ItemCurrencyEntity.EUR
    }

    private fun itemsCurrencyEntity(items: Set<Item>): CurrencyEntity {
        val itemCurrencies = items.map { it.currency }
        require(itemCurrencies.distinctBy { it }.size == itemCurrencies.size)
        return CurrencyEntity.valueOf(items.first().currency.name)
    }

}