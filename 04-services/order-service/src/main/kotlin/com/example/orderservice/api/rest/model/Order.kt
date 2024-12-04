package com.example.orderservice.api.rest.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.*

data class OrderSearchRequest(
    val userId: UUID
)

data class CreateOrderRequest(
    val userId: UUID,
    val items: Set<OrderItem>,
    val delivery: DeliveryRequest
)

data class UpdateOrderStatusRequest(
    val status: Status
)

data class Order(
    val id: UUID,
    val userId: UUID,
    val status: Status,
    val items: Set<OrderItem>,
    val totalQuantity: Int,
    val totalPrice: TotalPrice,
    @JsonInclude(Include.NON_NULL)
    @JsonProperty("_embedded")
    val embedded: Embedded?,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
) : Serializable

data class Embedded(
    val delivery: Delivery
) : Serializable

data class TotalPrice(
    val value: BigDecimal,
    val currency: Currency
) : Serializable

enum class Status {
    CREATED,
    IN_PROGRESS,
    DECLINED,
    CANCELLED,
    DELIVERED
}

enum class Currency {
    RUB, EUR
}