package com.example.orderservice.test

import com.example.orderservice.api.rest.model.*
import com.example.orderservice.repository.entity.*
import org.apache.commons.lang3.RandomStringUtils.randomNumeric
import java.time.OffsetDateTime
import java.util.*

object OrderTestData {

    fun orderEntity(
        status: StatusEntity = nextValue<StatusEntity>(),
        items: Set<ItemEntity> = setOf(itemEntity())
    ) = OrderEntity(
        userId = UUID.randomUUID(),
        status = status,
        items = items,
        totalQuantity = items.size,
        totalPrice = TotalPriceEntity(
            value = items.sumOf { it.price.value },
            currency = CurrencyEntity.RUB
        ),
        createdAt = OffsetDateTime.now(),
        updatedAt = OffsetDateTime.now()
    )

    fun itemEntity() = ItemEntity(
        id = UUID.randomUUID(),
        category = nextValue<ItemCategoryEntity>(),
        quantity = randomNumeric(3).toInt(),
        price = ItemPriceEntity(
            value = randomPrice(),
            currency = ItemCurrencyEntity.RUB
        )
    )

    fun createOrderRequest(
        items: Set<Item> = setOf(item())
    ) = CreateOrderRequest(
        userId = UUID.randomUUID(),
        items = items
    )

    fun item() = Item(
        id = UUID.randomUUID(),
        category = nextValue<ItemCategory>(),
        quantity = randomNumeric(3).toInt(),
        price = ItemPrice(
            value = randomPrice(),
            currency = ItemCurrency.RUB
        )
    )
}