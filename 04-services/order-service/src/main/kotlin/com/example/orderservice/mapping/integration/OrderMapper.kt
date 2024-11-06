package com.example.orderservice.mapping.integration

import com.example.orderservice.api.rest.model.Currency
import com.example.orderservice.api.rest.model.Order
import com.example.orderservice.api.rest.model.Status
import com.example.orderservice.api.rest.model.TotalPrice
import com.example.orderservice.mapping.integration.OrderItemMapper.toDomainModel

typealias DomainCurrency = com.example.orderservice.domain.kafka.client.model.Currency
typealias DomainOrder = com.example.orderservice.domain.kafka.client.model.Order
typealias DomainStatus = com.example.orderservice.domain.kafka.client.model.Status
typealias DomainTotalPrice = com.example.orderservice.domain.kafka.client.model.TotalPrice

object OrderMapper {

    internal fun Order.toDomainModel() = DomainOrder(
        id = id,
        userId = userId,
        status = status.toDomainStatus(),
        items = items.map { it.toDomainModel() }.toSet(),
        totalQuantity = totalQuantity,
        totalPrice = totalPrice.toDomainModel(),
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    internal fun Status.toDomainStatus() = when (this) {
        Status.CREATED -> DomainStatus.CREATED
        Status.IN_PROGRESS -> DomainStatus.IN_PROGRESS
        Status.DECLINED -> DomainStatus.DECLINED
        Status.CANCELLED -> DomainStatus.CANCELLED
        Status.DELIVERED -> DomainStatus.DELIVERED
    }

    internal fun TotalPrice.toDomainModel() = DomainTotalPrice(
        value = value,
        currency = currency.toDomainModel()
    )

    internal fun Currency.toDomainModel() = when (this) {
        Currency.RUB -> DomainCurrency.RUB
        Currency.EUR -> DomainCurrency.EUR
    }
}