package com.example.orderservice.mapping

import com.example.orderservice.api.rest.model.*
import com.example.orderservice.mapping.OrderItemMapper.toEntity
import com.example.orderservice.mapping.OrderItemMapper.toModel
import com.example.orderservice.repository.entity.*
import org.springframework.data.domain.Page
import org.springframework.data.web.PagedModel
import java.time.OffsetDateTime

object OrderMapper {

    fun OrderEntity.toModel() = Order(
        id = id!!,
        userId = userId,
        status = status.toModel(),
        items = items.map { it.toModel() }.toSet(),
        totalQuantity = totalQuantity,
        totalPrice = totalPrice.toModel(),
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    fun TotalPriceEntity.toModel() = TotalPrice(
        value = value,
        currency = currency.toModel()
    )

    fun Page<OrderEntity>.toPagedModel() = PagedModel(
        this.map { it.toModel() }
    )

    fun StatusEntity.toModel() = when (this) {
        StatusEntity.CREATED -> Status.CREATED
        StatusEntity.IN_PROGRESS -> Status.IN_PROGRESS
        StatusEntity.DECLINED -> Status.DECLINED
        StatusEntity.CANCELLED -> Status.CANCELLED
        StatusEntity.DELIVERED -> Status.DELIVERED
    }

    fun CurrencyEntity.toModel() = when (this) {
        CurrencyEntity.RUB -> Currency.RUB
        CurrencyEntity.EUR -> Currency.EUR
    }

    fun CreateOrderRequest.toEntity(): OrderEntity {
        val itemEntities = items.map { it.toEntity() }.toSet()
        val orderEntity = OrderEntity(
            userId = userId,
            status = StatusEntity.CREATED,
            totalQuantity = itemEntities.size,
            totalPrice = TotalPriceEntity(
                value = itemEntities.sumOf { it.price.value },
                currency = currencyEntity(itemEntities)
            ),
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        itemEntities.forEach { it.order = orderEntity }
        orderEntity.items.addAll(itemEntities)
        return orderEntity
    }

    fun Status.toEntity() = when (this) {
        Status.CREATED -> StatusEntity.CREATED
        Status.IN_PROGRESS -> StatusEntity.IN_PROGRESS
        Status.DECLINED -> StatusEntity.DECLINED
        Status.CANCELLED -> StatusEntity.CANCELLED
        Status.DELIVERED -> StatusEntity.DELIVERED
    }

    private fun currencyEntity(items: Set<OrderItemEntity>): CurrencyEntity {
        val itemCurrencies = items.map { it.price.currency }
        require(itemCurrencies.distinctBy { it }.size == itemCurrencies.size)
        return CurrencyEntity.valueOf(items.first().price.currency.name)
    }

}