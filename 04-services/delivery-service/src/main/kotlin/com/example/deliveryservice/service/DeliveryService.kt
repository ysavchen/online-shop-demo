package com.example.deliveryservice.service

import com.example.deliveryservice.kafka.client.model.CreateDeliveryRequest
import com.example.deliveryservice.kafka.client.model.DeliveryCreatedResponse
import com.example.deliveryservice.kafka.client.model.RequestDeliveryMessage
import com.example.deliveryservice.mapping.DeliveryMapper.toEntity
import com.example.deliveryservice.mapping.DeliveryMapper.toModel
import com.example.deliveryservice.repository.DeliveryRepository
import com.example.deliveryservice.repository.IdempotencyKeyRepository
import com.example.deliveryservice.repository.entity.IdempotencyKeyEntity
import com.example.deliveryservice.repository.entity.ResourceEntity
import com.example.deliveryservice.response.kafka.client.ResponseDeliveryKafkaProducer
import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.TransactionTemplate
import java.util.*

@Service
class DeliveryService(
    private val deliveryRepository: DeliveryRepository,
    private val idempotencyKeyRepository: IdempotencyKeyRepository,
    private val transactionManager: PlatformTransactionManager,
    private val kafkaProducer: ResponseDeliveryKafkaProducer
) {

    companion object {
        private val logger = KotlinLogging.logger(DeliveryService::class.java.name)
    }

    private val transactionTemplate = TransactionTemplate(transactionManager)

    fun processMessage(message: ConsumerRecord<UUID, RequestDeliveryMessage>) {
        val key = idempotencyKeyRepository.findByIdOrNull(message.key())
        if (key != null) {
            logger.debug { "Duplicate message with key=${key.idempotencyKey}, message already processed" }
            return
        }

        when (val request = message.value()) {
            is CreateDeliveryRequest -> processRequest(message.key(), request)
        }
    }

    private fun processRequest(messageKey: UUID, request: CreateDeliveryRequest) {
        val delivery = transactionTemplate.execute {
            val savedDelivery = deliveryRepository.save(request.data.toEntity())
            idempotencyKeyRepository.save(
                IdempotencyKeyEntity(messageKey, savedDelivery.id!!, ResourceEntity.DELIVERY)
            )
            savedDelivery.toModel()
        }!!
        kafkaProducer.send(DeliveryCreatedResponse(delivery))
    }
}