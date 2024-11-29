package com.example.deliveryservice.api.kafka.client

import com.example.deliveryservice.api.kafka.client.model.ResponseMessage
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import java.util.*
import java.util.concurrent.CompletableFuture

interface ResponseDeliveryKafkaProducer {

    fun send(message: ResponseMessage): CompletableFuture<SendResult<UUID, ResponseMessage>>
}

class ResponseDeliveryKafkaProducerImpl(
    private val kafkaTemplate: KafkaTemplate<UUID, ResponseMessage>,
    private val enabled: Boolean
) : ResponseDeliveryKafkaProducer {

    override fun send(message: ResponseMessage): CompletableFuture<SendResult<UUID, ResponseMessage>> =
        if (enabled) {
            kafkaTemplate.sendDefault(UUID.randomUUID(), message)
        } else {
            CompletableFuture.failedFuture(UnsupportedOperationException("ResponseDeliveryKafkaProducer is disabled"))
        }
}