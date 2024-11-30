package com.example.deliveryservice.mapping

import com.example.deliveryservice.kafka.client.model.Address
import com.example.deliveryservice.repository.entity.AddressEntity

object AddressMapper {

    internal fun Address.toEntity() = AddressEntity(
        country = country,
        city = city,
        street = street,
        building = building
    )

    internal fun AddressEntity.toModel() = Address(
        country = country,
        city = city,
        street = street,
        building = building
    )
}