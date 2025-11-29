package com.example.orderservice.api.rest.model

import com.example.online.shop.model.Building
import com.example.online.shop.model.City
import com.example.online.shop.model.Country
import com.example.online.shop.model.Street
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
    val country: Country,
    val city: City,
    val street: Street,
    val building: Building
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