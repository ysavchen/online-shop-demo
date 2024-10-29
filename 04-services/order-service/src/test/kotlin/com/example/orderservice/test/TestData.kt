package com.example.orderservice.test

import com.example.orderservice.api.rest.model.*
import com.example.orderservice.repository.entity.CurrencyEntity
import com.example.orderservice.repository.entity.OrderEntity
import com.example.orderservice.repository.entity.StatusEntity
import com.example.orderservice.repository.entity.TotalPriceEntity
import org.apache.commons.lang3.RandomStringUtils.randomNumeric
import java.time.OffsetDateTime
import java.util.*

object OrderTestData {

    fun orderEntity(
        status: StatusEntity = nextValue<StatusEntity>(),
        orderItems: Set<OrderItem> = setOf(orderItem())
    ) = OrderEntity(
        userId = UUID.randomUUID(),
        status = status,
        totalQuantity = orderItems.sumOf { it.quantity },
        totalPrice = TotalPriceEntity(
            value = orderItems.sumOf { it.price.value },
            currency = nextValue<CurrencyEntity>()
        ),
        createdAt = OffsetDateTime.now(),
        updatedAt = OffsetDateTime.now()
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