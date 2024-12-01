package com.example.deliveryservice.service

import com.example.deliveryservice.kafka.client.model.CreateDeliveryRequest
import com.example.deliveryservice.kafka.client.model.DeliveryCreatedResponse
import com.example.deliveryservice.kafka.client.model.RequestDeliveryMessage
import com.example.deliveryservice.kafka.client.model.ResponseDeliveryMessage
import com.example.deliveryservice.mapping.DeliveryMapper.toEntity
import com.example.deliveryservice.mapping.DeliveryMapper.toModel
import com.example.deliveryservice.repository.DeliveryRepository
import com.example.deliveryservice.repository.IdempotencyKeyRepository
import com.example.deliveryservice.repository.entity.IdempotencyKeyEntity
import com.example.deliveryservice.repository.entity.ResourceEntity.DELIVERY
import jakarta.transaction.Transactional
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
class DeliveryService(
    private val deliveryRepository: DeliveryRepository,
    private val idempotencyKeyRepository: IdempotencyKeyRepository
) {

    @Transactional
    fun processMessage(message: ConsumerRecord<UUID, RequestDeliveryMessage>): ResponseDeliveryMessage {
        val key = idempotencyKeyRepository.findByIdOrNull(message.key())
        if (key != null) {
            throw DuplicateMessageException(key.idempotencyKey)
        }

        return when (val request = message.value()) {
            is CreateDeliveryRequest -> processRequest(message.key(), request)
        }
    }

    private fun processRequest(messageKey: UUID, request: CreateDeliveryRequest): ResponseDeliveryMessage {
        val deliveryEntity = request.data.toEntity()
        val delivery = deliveryRepository.save(deliveryEntity).toModel()
        idempotencyKeyRepository.save(IdempotencyKeyEntity(messageKey, delivery.id, DELIVERY))
        return DeliveryCreatedResponse(delivery)
    }
}