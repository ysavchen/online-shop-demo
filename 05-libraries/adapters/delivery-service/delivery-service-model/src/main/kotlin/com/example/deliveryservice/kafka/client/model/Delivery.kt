package com.example.deliveryservice.kafka.client.model

import java.util.*

data class CreateDeliveryRequestData(
    val type: Type,
    val address: Address,
    val orderId: UUID
) : Data

data class Delivery(
    val id: UUID,
    val type: Type,
    val status: Status,
    val address: Address,
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