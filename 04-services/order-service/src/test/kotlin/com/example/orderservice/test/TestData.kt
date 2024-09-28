package com.example.orderservice.test

import com.example.orderservice.api.rest.model.*
import com.example.orderservice.repository.entity.*
import org.apache.commons.lang3.RandomStringUtils.randomNumeric
import java.time.OffsetDateTime
import java.util.*

object OrderTestData {

    fun orderEntity(
        status: StatusEntity = nextValue<StatusEntity>(),
        orderItemEntities: Set<OrderItemEntity> = setOf(orderItemEntity())
    ): OrderEntity {
        val orderEntity = OrderEntity(
            userId = UUID.randomUUID(),
            status = status,
            totalQuantity = orderItemEntities.size,
            totalPrice = TotalPriceEntity(
                value = orderItemEntities.sumOf { it.price.value },
                currency = CurrencyEntity.RUB
            ),
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        orderEntity.addItems(orderItemEntities)
        return orderEntity
    }

    fun orderItemEntity() = OrderItemEntity(
        id = UUID.randomUUID(),
        category = nextValue<ItemCategoryEntity>(),
        quantity = randomNumeric(3).toInt(),
        price = ItemPriceEntity(
            value = randomPrice(),
            currency = ItemCurrencyEntity.RUB
        )
    )

    fun createOrderRequest(
        items: Set<OrderItem> = setOf(orderItem())
    ) = CreateOrderRequest(
        userId = UUID.randomUUID(),
        items = items
    )

    fun orderItem() = OrderItem(
        id = UUID.randomUUID(),
        category = nextValue<ItemCategory>(),
        quantity = randomNumeric(3).toInt(),
        price = ItemPrice(
            value = randomPrice(),
            currency = ItemCurrency.RUB
        )
    )
}