package com.example.deliveryservice.mapping

import com.example.deliveryservice.kafka.client.model.CreateDeliveryRequestData
import com.example.deliveryservice.kafka.client.model.Delivery
import com.example.deliveryservice.kafka.client.model.Status
import com.example.deliveryservice.kafka.client.model.Type
import com.example.deliveryservice.repository.entity.DeliveryEntity
import com.example.deliveryservice.repository.entity.StatusEntity
import com.example.deliveryservice.repository.entity.TypeEntity
import java.time.OffsetDateTime

object DeliveryMapper {

    internal fun CreateDeliveryRequestData.toEntity() = DeliveryEntity(
        type = type.toEntity(),
        status = StatusEntity.CREATED,
        address = address,
        orderId = orderId,
        createdAt = OffsetDateTime.now(),
        updatedAt = OffsetDateTime.now()
    )

    internal fun DeliveryEntity.toModel() = Delivery(
        id = id!!,
        type = type.toModel(),
        status = status.toModel(),
        address = address,
        orderId = orderId
    )

    private fun Type.toEntity() = when (this) {
        Type.HOME_DELIVERY -> TypeEntity.HOME_DELIVERY
        Type.PICKUP_POINT -> TypeEntity.PICKUP_POINT
        Type.PARCEL_LOCKER -> TypeEntity.PARCEL_LOCKER
        Type.IN_STORE_PICKUP -> TypeEntity.IN_STORE_PICKUP
    }

    private fun TypeEntity.toModel() = when (this) {
        TypeEntity.HOME_DELIVERY -> Type.HOME_DELIVERY
        TypeEntity.PICKUP_POINT -> Type.PICKUP_POINT
        TypeEntity.PARCEL_LOCKER -> Type.PARCEL_LOCKER
        TypeEntity.IN_STORE_PICKUP -> Type.IN_STORE_PICKUP
    }

    private fun StatusEntity.toModel() = when (this) {
        StatusEntity.CREATED -> Status.CREATED
        StatusEntity.IN_PROGRESS -> Status.IN_PROGRESS
        StatusEntity.DELIVERED -> Status.DELIVERED
    }
}