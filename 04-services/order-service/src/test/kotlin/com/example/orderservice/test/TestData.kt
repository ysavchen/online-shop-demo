package com.example.orderservice.test

import com.example.orderservice.api.rest.model.*
import com.example.orderservice.repository.entity.*
import org.apache.commons.lang3.RandomStringUtils.randomNumeric
import java.time.OffsetDateTime
import java.util.*

object OrderTestData {

    fun orderEntity(
        status: StatusEntity = nextValue<StatusEntity>(),
        orderItemEntities: Set<OrderItem> = setOf(orderItem())
    ) = OrderEntity(
        userId = UUID.randomUUID(),
        status = status,
        totalQuantity = orderItemEntities.size,
        totalPrice = TotalPriceEntity(
            value = orderItemEntities.sumOf { it.price.value },
            currency = nextValue<CurrencyEntity>()
        ),
        createdAt = OffsetDateTime.now(),
        updatedAt = OffsetDateTime.now()
    )

    fun orderItemEntity(orderId: UUID) = OrderItemEntity(
        orderItemId = OrderItemId(UUID.randomUUID(), orderId),
        category = nextValue<ItemCategoryEntity>(),
        quantity = randomNumeric(3).toInt(),
        price = ItemPriceEntity(
            value = randomPrice(),
            currency = nextValue<ItemCurrencyEntity>()
        )
    )

    fun createOrderRequest(
        items: Set<OrderItem> = setOf(orderItem())
    ) = CreateOrderRequest(
        userId = UUID.randomUUID(),
        items = items
    )

    fun orderItem(
        currency: ItemCurrency = nextValue<ItemCurrency>()
    ) = OrderItem(
        id = UUID.randomUUID(),
        category = nextValue<ItemCategory>(),
        quantity = randomNumeric(3).toInt(),
        price = ItemPrice(
            value = randomPrice(),
            currency = currency
        )
    )
}