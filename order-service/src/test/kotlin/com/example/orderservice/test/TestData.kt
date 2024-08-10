package com.example.orderservice.test

import com.example.orderservice.repository.entity.*
import org.apache.commons.lang3.RandomStringUtils.randomNumeric
import java.time.OffsetDateTime
import java.util.*

object OrderTestData {

    fun orderEntity(items: Set<ItemEntity> = setOf(itemEntity())) = OrderEntity(
        userId = UUID.randomUUID(),
        status = nextValue<StatusEntity>(),
        items = setOf(),
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
}