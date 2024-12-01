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
    private val replyTopic: String,
    private val kafkaTemplate: KafkaTemplate<UUID, ResponseDeliveryMessage>,
) : ResponseDeliveryKafkaProducer {

    override fun send(correlationId: Header, message: ResponseDeliveryMessage): CompletableFuture<SendResult<UUID, ResponseDeliveryMessage>> {
        val record = ProducerRecord<UUID, ResponseDeliveryMessage>(replyTopic, UUID.randomUUID(), message)
            .apply { headers().add(correlationId) }
        return kafkaTemplate.send(record)
    }
}