package com.example.deliveryservice.service

import com.example.deliveryservice.kafka.client.model.*
import com.example.deliveryservice.mapping.DeliveryMapper.toEntity
import com.example.deliveryservice.mapping.DeliveryMapper.toModel
import com.example.deliveryservice.repository.DeliveryRepository
import com.example.deliveryservice.repository.ProcessedMessageRepository
import com.example.deliveryservice.repository.entity.ProcessedMessageEntity
import com.example.deliveryservice.repository.entity.ResourceTypeEntity.DELIVERY
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class DeliveryService(
    private val deliveryRepository: DeliveryRepository,
    private val messageRepository: ProcessedMessageRepository
) {

    @Transactional(readOnly = true)
    fun getDeliveryById(deliveryId: UUID): ReplyDeliveryMessage {
        val delivery = deliveryRepository.findByIdOrNull(deliveryId)?.toModel()
        return if (delivery != null) {
            DeliveryDataReply(delivery)
        } else DeliveryNotFoundErrorReply("Delivery not found by id=$deliveryId")
    }

    @Transactional(readOnly = true)
    fun getDeliveryByOrderId(orderId: UUID): ReplyDeliveryMessage {
        val delivery = deliveryRepository.findDeliveryByOrderId(orderId)?.toModel()
        return if (delivery != null) {
            DeliveryDataReply(delivery)
        } else DeliveryNotFoundErrorReply("Delivery not found by orderId=$orderId")
    }

    @Transactional
    fun createDelivery(messageKey: UUID, request: CreateDeliveryRequest): ReplyDeliveryMessage {
        val error = validateMessage(messageKey)
        if (error != null) return error

        val deliveryEntity = request.data.toEntity()
        val delivery = deliveryRepository.save(deliveryEntity).toModel()
        messageRepository.save(ProcessedMessageEntity(messageKey, delivery.id, DELIVERY))
        return DeliveryDataReply(delivery)
    }

    private fun validateMessage(messageKey: UUID): DuplicateMessageErrorReply? {
        val processedMessage = messageRepository.findByIdOrNull(messageKey)
        return processedMessage?.let {
            DuplicateMessageErrorReply(
                messageKey = processedMessage.messageKey,
                resourceId = processedMessage.resourceId,
                resource = processedMessage.resourceType.name.lowercase()
            )
        }
    }
}