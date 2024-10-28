package com.example.orderservice.mapping

import com.example.orderservice.api.rest.model.*
import com.example.orderservice.mapping.OrderItemMapper.toModel
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

    internal fun TotalPriceEntity.toModel() = TotalPrice(
        value = value,
        currency = currency.toModel()
    )

    internal fun Page<OrderEntity>.toPagedModel() = PagedModel(
        this.map { it.toModel() }
    )

    internal fun StatusEntity.toModel() = when (this) {
        StatusEntity.CREATED -> Status.CREATED
        StatusEntity.IN_PROGRESS -> Status.IN_PROGRESS
        StatusEntity.DECLINED -> Status.DECLINED
        StatusEntity.CANCELLED -> Status.CANCELLED
        StatusEntity.DELIVERED -> Status.DELIVERED
    }

    internal fun CurrencyEntity.toModel() = when (this) {
        CurrencyEntity.RUB -> Currency.RUB
        CurrencyEntity.EUR -> Currency.EUR
    }

    internal fun CreateOrderRequest.toEntity() = OrderEntity(
        userId = userId,
        status = StatusEntity.CREATED,
        totalQuantity = items.size,
        totalPrice = TotalPriceEntity(
            value = items.sumOf { it.price.value },
            currency = currencyEntity(items)
        ),
        createdAt = OffsetDateTime.now(),
        updatedAt = OffsetDateTime.now()
    )

    internal fun Status.toEntity() = when (this) {
        Status.CREATED -> StatusEntity.CREATED
        Status.IN_PROGRESS -> StatusEntity.IN_PROGRESS
        Status.DECLINED -> StatusEntity.DECLINED
        Status.CANCELLED -> StatusEntity.CANCELLED
        Status.DELIVERED -> StatusEntity.DELIVERED
    }

    private fun currencyEntity(items: Set<OrderItem>): CurrencyEntity {
        val itemCurrencies = items.map { it.price.currency }
        require(itemCurrencies.distinctBy { it }.size == itemCurrencies.size)
        return CurrencyEntity.valueOf(items.first().price.currency.name)
    }

}