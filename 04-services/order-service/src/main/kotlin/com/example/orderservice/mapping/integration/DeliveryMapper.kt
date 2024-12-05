package com.example.orderservice.mapping.integration

import com.example.deliveryservice.kafka.client.model.Address
import com.example.deliveryservice.kafka.client.model.CreateDeliveryRequest
import com.example.deliveryservice.kafka.client.model.Type
import com.example.orderservice.api.rest.model.DeliveryAddress
import com.example.orderservice.api.rest.model.DeliveryRequest
import com.example.orderservice.api.rest.model.DeliveryType
import java.util.*

object DeliveryMapper {

    internal fun DeliveryRequest.toKafkaModel(orderId: UUID) =
        CreateDeliveryRequest(
            type = type.toKafkaModel(),
            address = address.toKafkaModel(),
            orderId = orderId
        )

    internal fun DeliveryAddress.toKafkaModel() = Address(
        country = country,
        city = city,
        street = street,
        building = building
    )

    private fun DeliveryType.toKafkaModel() = when (this) {
        DeliveryType.HOME_DELIVERY -> Type.HOME_DELIVERY
        DeliveryType.IN_STORE_PICKUP -> Type.IN_STORE_PICKUP
    }
}