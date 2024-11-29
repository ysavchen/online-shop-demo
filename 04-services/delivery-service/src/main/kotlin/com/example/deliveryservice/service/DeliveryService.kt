package com.example.deliveryservice.service

import com.example.deliveryservice.api.kafka.client.model.RequestMessage
import com.example.deliveryservice.repository.DeliveryRepository
import com.example.deliveryservice.repository.IdempotencyKeyRepository
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class DeliveryService(
    private val deliveryRepository: DeliveryRepository,
    private val idempotencyKeyRepository: IdempotencyKeyRepository
) {

    @Transactional
    fun createDelivery(message: ConsumerRecord<UUID, RequestMessage>) {

    }
}