package com.example.deliveryservice.api.kafka.client

import com.example.deliveryservice.api.kafka.client.model.RequestMessage
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import java.util.*
import java.util.concurrent.CompletableFuture

interface RequestDeliveryKafkaProducer {

    fun send(message: RequestMessage): CompletableFuture<SendResult<UUID, RequestMessage>>
}

class RequestDeliveryKafkaProducerImpl(
    private val kafkaTemplate: KafkaTemplate<UUID, RequestMessage>,
    private val enabled: Boolean
) : RequestDeliveryKafkaProducer {

    override fun send(message: RequestMessage): CompletableFuture<SendResult<UUID, RequestMessage>> =
        if (enabled) {
            kafkaTemplate.sendDefault(UUID.randomUUID(), message)
        } else {
            CompletableFuture.failedFuture(UnsupportedOperationException("RequestDeliveryKafkaProducer is disabled"))
        }
}