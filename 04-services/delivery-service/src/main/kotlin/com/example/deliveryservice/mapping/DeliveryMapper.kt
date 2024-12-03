package com.example.deliveryservice.mapping

import com.example.deliveryservice.kafka.client.model.CreateDelivery
import com.example.deliveryservice.kafka.client.model.Delivery
import com.example.deliveryservice.kafka.client.model.Status
import com.example.deliveryservice.kafka.client.model.Type
import com.example.deliveryservice.mapping.AddressMapper.toEntity
import com.example.deliveryservice.mapping.AddressMapper.toModel
import com.example.deliveryservice.repository.entity.DeliveryEntity
import com.example.deliveryservice.repository.entity.StatusEntity
import com.example.deliveryservice.repository.entity.TypeEntity
import java.time.LocalDate
import java.time.OffsetDateTime

object DeliveryMapper {

    internal fun CreateDelivery.toEntity() = DeliveryEntity(
        type = type.toEntity(),
        date = LocalDate.now().plusDays(3),
        address = address.toEntity(),
        status = StatusEntity.CREATED,
        orderId = orderId,
        createdAt = OffsetDateTime.now(),
        updatedAt = OffsetDateTime.now()
    )

    internal fun DeliveryEntity.toModel() = Delivery(
        id = id!!,
        type = type.toModel(),
        date = date,
        address = address.toModel(),
        status = status.toModel(),
        orderId = orderId
    )

    private fun Type.toEntity() = when (this) {
        Type.HOME_DELIVERY -> TypeEntity.HOME_DELIVERY
        Type.IN_STORE_PICKUP -> TypeEntity.IN_STORE_PICKUP
    }

    private fun TypeEntity.toModel() = when (this) {
        TypeEntity.HOME_DELIVERY -> Type.HOME_DELIVERY
        TypeEntity.IN_STORE_PICKUP -> Type.IN_STORE_PICKUP
    }

    private fun StatusEntity.toModel() = when (this) {
        StatusEntity.CREATED -> Status.CREATED
        StatusEntity.IN_PROGRESS -> Status.IN_PROGRESS
        StatusEntity.DELIVERED -> Status.DELIVERED
    }
}