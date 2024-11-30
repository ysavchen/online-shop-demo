package com.example.orderservice.api.rest.model

data class Delivery(
    val type: DeliveryType,
    val address: Address
)

data class Address(
    val country: String,
    val city: String,
    val street: String,
    val building: String
)

enum class DeliveryType {
    HOME_DELIVERY,
    IN_STORE_PICKUP
}