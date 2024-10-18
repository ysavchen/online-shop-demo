package com.example.deliveryservice.repository.entity

sealed class AddressEntity {

    data class HomeAddressEntity(
        val city: String,
        val street: String,
        val building: String,
        val apartment: String?
    ) : AddressEntity()
}