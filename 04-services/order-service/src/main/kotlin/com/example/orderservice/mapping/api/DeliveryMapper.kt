package com.example.orderservice.mapping.api

import com.example.deliveryservice.kafka.client.model.Address
import com.example.deliveryservice.kafka.client.model.Delivery
import com.example.deliveryservice.kafka.client.model.Status
import com.example.deliveryservice.kafka.client.model.Type
import com.example.orderservice.api.rest.model.DeliveryAddress
import com.example.orderservice.api.rest.model.DeliveryStatus
import com.example.orderservice.api.rest.model.DeliveryType

typealias OrderDelivery = com.example.orderservice.api.rest.model.Delivery

object DeliveryMapper {

    internal fun Delivery.toModel() = OrderDelivery(
        id = id,
        type = type.toModel(),
        address = address.toModel(),
        status = status.toModel()
    )

    internal fun Address.toModel() = DeliveryAddress(
        country = country,
        city = city,
        street = street,
        building = building
    )

    internal fun Type.toModel() = when (this) {
        Type.HOME_DELIVERY -> DeliveryType.HOME_DELIVERY
        Type.IN_STORE_PICKUP -> DeliveryType.IN_STORE_PICKUP
    }

    internal fun Status.toModel() = when (this) {
        Status.CREATED -> DeliveryStatus.CREATED
        Status.IN_PROGRESS -> DeliveryStatus.IN_PROGRESS
        Status.DELIVERED -> DeliveryStatus.DELIVERED
    }
}