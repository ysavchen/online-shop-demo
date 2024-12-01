package com.example.deliveryservice.response.kafka.client

import com.example.deliveryservice.kafka.client.model.ResponseDeliveryMessage
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.header.Header
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import java.util.*
import java.util.concurrent.CompletableFuture

interface ResponseDeliveryKafkaProducer {

    fun send(correlationId: Header, message: ResponseDeliveryMessage): CompletableFuture<SendResult<UUID, ResponseDeliveryMessage>>
}

class ResponseDeliveryKafkaProducerImpl(
    private val enabled: Boolean,
    private val responseTopic: String,
    private val kafkaTemplate: KafkaTemplate<UUID, ResponseDeliveryMessage>,
) : ResponseDeliveryKafkaProducer {

    override fun send(correlationId: Header, message: ResponseDeliveryMessage): CompletableFuture<SendResult<UUID, ResponseDeliveryMessage>> =
        if (enabled) {
            val record = ProducerRecord<UUID, ResponseDeliveryMessage>(responseTopic, UUID.randomUUID(), message)
                .apply { headers().add(correlationId) }
            kafkaTemplate.send(record)
        } else {
            CompletableFuture.failedFuture(UnsupportedOperationException("ResponseDeliveryKafkaProducer is disabled"))
        }
}