package com.example.deliveryservice.request.kafka.client

import com.example.deliveryservice.kafka.client.model.RequestDeliveryMessage
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import java.util.*
import java.util.concurrent.CompletableFuture

interface RequestDeliveryKafkaProducer {

    fun send(message: RequestDeliveryMessage): CompletableFuture<SendResult<UUID, RequestDeliveryMessage>>
}

class RequestDeliveryKafkaProducerImpl(
    private val kafkaTemplate: KafkaTemplate<UUID, RequestDeliveryMessage>,
    private val enabled: Boolean
) : RequestDeliveryKafkaProducer {

    override fun send(message: RequestDeliveryMessage): CompletableFuture<SendResult<UUID, RequestDeliveryMessage>> =
        if (enabled) {
            kafkaTemplate.sendDefault(UUID.randomUUID(), message)
        } else {
            CompletableFuture.failedFuture(UnsupportedOperationException("RequestDeliveryKafkaProducer is disabled"))
        }
}