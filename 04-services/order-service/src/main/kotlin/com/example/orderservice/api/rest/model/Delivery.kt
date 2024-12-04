package com.example.orderservice.api.rest.model

import java.time.LocalDate
import java.util.*

data class DeliveryRequest(
    val type: DeliveryType,
    val address: DeliveryAddress
)

data class Delivery(
    val id: UUID,
    val type: DeliveryType,
    val date: LocalDate,
    val address: DeliveryAddress,
    val status: DeliveryStatus
)

data class DeliveryAddress(
    val country: String,
    val city: String,
    val street: String,
    val building: String
)

enum class DeliveryType {
    HOME_DELIVERY,
    IN_STORE_PICKUP
}

enum class DeliveryStatus {
    CREATED,
    IN_PROGRESS,
    DELIVERED
}