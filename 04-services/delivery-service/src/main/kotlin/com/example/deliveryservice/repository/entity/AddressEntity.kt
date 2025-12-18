package com.example.deliveryservice.repository.entity

import com.example.online.shop.model.Building
import com.example.online.shop.model.City
import com.example.online.shop.model.Country
import com.example.online.shop.model.Street
import jakarta.persistence.Embeddable

@Embeddable
data class AddressEntity(
    val country: Country,
    val city: City,
    val street: Street,
    val building: Building
)