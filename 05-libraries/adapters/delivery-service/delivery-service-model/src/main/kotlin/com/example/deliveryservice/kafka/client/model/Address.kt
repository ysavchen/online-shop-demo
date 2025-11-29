package com.example.deliveryservice.kafka.client.model

import com.example.online.shop.model.Building
import com.example.online.shop.model.City
import com.example.online.shop.model.Country
import com.example.online.shop.model.Street

data class Address(
    val country: Country,
    val city: City,
    val street: Street,
    val building: Building
)