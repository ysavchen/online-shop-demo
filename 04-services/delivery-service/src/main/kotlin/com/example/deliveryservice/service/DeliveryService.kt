package com.example.deliveryservice.service

import com.example.deliveryservice.kafka.client.model.*
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
    fun processMessage(message: ConsumerRecord<UUID, RequestDeliveryMessage>): ReplyDeliveryMessage {
        val key = idempotencyKeyRepository.findByIdOrNull(message.key())
        if (key != null) {
            val error = DuplicateMessageError(key.idempotencyKey, key.resourceId, key.resource.name.lowercase())
            return ClientErrorReply(error)
        }

        return when (val request = message.value()) {
            is GetDeliveryByIdRequest -> processRequest(request)
            is CreateDeliveryRequest -> processRequest(message.key(), request)
        }
    }

    private fun processRequest(request: GetDeliveryByIdRequest): ReplyDeliveryMessage {
        val id = request.data.deliveryId
        val delivery = deliveryRepository.findByIdOrNull(id)?.toModel()
        return if (delivery != null) {
            DeliveryDataReply(delivery)
        } else ClientErrorReply(DeliveryNotFoundError(id))
    }

    private fun processRequest(messageKey: UUID, request: CreateDeliveryRequest): ReplyDeliveryMessage {
        val deliveryEntity = request.data.toEntity()
        val delivery = deliveryRepository.save(deliveryEntity).toModel()
        idempotencyKeyRepository.save(IdempotencyKeyEntity(messageKey, delivery.id, DELIVERY))
        return DeliveryDataReply(delivery)
    }
}