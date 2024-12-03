package com.example.deliveryservice.repository

import com.example.deliveryservice.repository.entity.DeliveryEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface DeliveryRepository : JpaRepository<DeliveryEntity, UUID> {

    fun findDeliveryByOrderId(orderId: UUID): DeliveryEntity?
}