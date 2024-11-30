package com.example.deliveryservice.api.kafka.client

import com.example.deliveryservice.api.kafka.client.model.ResponseDeliveryMessage
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import java.util.*
import java.util.concurrent.CompletableFuture

interface ResponseDeliveryKafkaProducer {

    fun send(message: ResponseDeliveryMessage): CompletableFuture<SendResult<UUID, ResponseDeliveryMessage>>
}

class ResponseDeliveryKafkaProducerImpl(
    private val kafkaTemplate: KafkaTemplate<UUID, ResponseDeliveryMessage>,
    private val enabled: Boolean
) : ResponseDeliveryKafkaProducer {

    override fun send(message: ResponseDeliveryMessage): CompletableFuture<SendResult<UUID, ResponseDeliveryMessage>> =
        if (enabled) {
            kafkaTemplate.sendDefault(UUID.randomUUID(), message)
        } else {
            CompletableFuture.failedFuture(UnsupportedOperationException("ResponseDeliveryKafkaProducer is disabled"))
        }
}