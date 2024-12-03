package com.example.deliveryservice.kafka.client.model

import java.time.LocalDate
import java.util.*

data class CreateDelivery(
    val type: Type,
    val address: Address,
    val orderId: UUID
) : Data

data class GetDeliveryById(
    val deliveryId: UUID
) : Data

data class GetDeliveryByOrderId(
    val orderId: UUID
) : Data

data class Delivery(
    val id: UUID,
    val type: Type,
    val date: LocalDate,
    val address: Address,
    val status: Status,
    val orderId: UUID
) : Data

enum class Type {
    HOME_DELIVERY,
    IN_STORE_PICKUP
}

enum class Status {
    CREATED,
    IN_PROGRESS,
    DELIVERED
}