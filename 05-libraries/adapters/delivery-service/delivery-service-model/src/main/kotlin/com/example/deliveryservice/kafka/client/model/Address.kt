package com.example.deliveryservice.kafka.client.model

data class Address(
    val country: String,
    val city: String,
    val street: String,
    val building: String
)