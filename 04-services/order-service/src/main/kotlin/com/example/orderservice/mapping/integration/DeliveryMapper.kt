package com.example.orderservice.mapping.integration

import com.example.deliveryservice.kafka.client.model.Address
import com.example.deliveryservice.kafka.client.model.CreateDeliveryRequestData
import com.example.deliveryservice.kafka.client.model.Type
import com.example.orderservice.api.rest.model.Delivery
import com.example.orderservice.api.rest.model.DeliveryType
import java.util.*

typealias DeliveryAddress = com.example.orderservice.api.rest.model.Address

object DeliveryMapper {

    internal fun Delivery.toKafkaModel(orderId: UUID) =
        CreateDeliveryRequestData(
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