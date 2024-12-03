package com.example.deliveryservice.service

import com.example.deliveryservice.kafka.client.model.*
import com.example.deliveryservice.mapping.DeliveryMapper.toEntity
import com.example.deliveryservice.mapping.DeliveryMapper.toModel
import com.example.deliveryservice.repository.DeliveryRepository
import com.example.deliveryservice.repository.ProcessedMessageRepository
import com.example.deliveryservice.repository.entity.ProcessedMessageEntity
import com.example.deliveryservice.repository.entity.ResourceTypeEntity.DELIVERY
import jakarta.transaction.Transactional
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
class DeliveryService(
    private val deliveryRepository: DeliveryRepository,
    private val processedMessageRepository: ProcessedMessageRepository
) {

    @Transactional
    fun processMessage(message: ConsumerRecord<UUID, RequestDeliveryMessage>): ReplyDeliveryMessage =
        when (val request = message.value()) {
            is GetDeliveryByIdRequest -> processRequest(request)
            is GetDeliveryByOrderIdRequest -> processRequest(request)
            is CreateDeliveryRequest -> processRequest(message.key(), request)
        }

    private fun processRequest(request: GetDeliveryByIdRequest): ReplyDeliveryMessage {
        val id = request.data.deliveryId
        val delivery = deliveryRepository.findByIdOrNull(id)?.toModel()
        return if (delivery != null) {
            DeliveryDataReply(delivery)
        } else DeliveryNotFoundErrorReply(DeliveryNotFoundError("Delivery not found by id=$id"))
    }

    private fun processRequest(request: GetDeliveryByOrderIdRequest): ReplyDeliveryMessage {
        val id = request.data.orderId
        val delivery = deliveryRepository.findDeliveryByOrderId(id)?.toModel()
        return if (delivery != null) {
            DeliveryDataReply(delivery)
        } else DeliveryNotFoundErrorReply(DeliveryNotFoundError("Delivery not found by orderId=$id"))
    }

    private fun processRequest(messageKey: UUID, request: CreateDeliveryRequest): ReplyDeliveryMessage {
        val error = validateMessage(messageKey)
        if (error != null) return error

        val deliveryEntity = request.data.toEntity()
        val delivery = deliveryRepository.save(deliveryEntity).toModel()
        processedMessageRepository.save(ProcessedMessageEntity(messageKey, delivery.id, DELIVERY))
        return DeliveryDataReply(delivery)
    }

    private fun validateMessage(messageKey: UUID): DuplicateMessageErrorReply? {
        val processedMessage = processedMessageRepository.findByIdOrNull(messageKey)
        return if (processedMessage != null) {
            val error = DuplicateMessageError(
                messageKey = processedMessage.messageKey,
                resourceId = processedMessage.resourceId,
                resource = processedMessage.resourceType.name.lowercase()
            )
            DuplicateMessageErrorReply(error)
        } else null
    }
}