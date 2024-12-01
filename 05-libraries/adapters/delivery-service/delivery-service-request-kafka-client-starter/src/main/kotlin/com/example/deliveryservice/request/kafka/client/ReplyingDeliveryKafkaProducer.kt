package com.example.deliveryservice.request.kafka.client

import com.example.deliveryservice.kafka.client.model.ReplyDeliveryMessage
import com.example.deliveryservice.kafka.client.model.RequestDeliveryMessage
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate
import java.util.UUID
import java.util.concurrent.CompletableFuture

interface ReplyingDeliveryKafkaProducer {

    fun sendAndReceive(message: RequestDeliveryMessage): CompletableFuture<ConsumerRecord<UUID, ReplyDeliveryMessage>>
}

class ReplyingDeliveryKafkaProducerImpl(
    private val enabled: Boolean,
    private val requestTopic: String,
    private val kafkaTemplate: ReplyingKafkaTemplate<UUID, RequestDeliveryMessage, ReplyDeliveryMessage>
) : ReplyingDeliveryKafkaProducer {

    override fun sendAndReceive(message: RequestDeliveryMessage): CompletableFuture<ConsumerRecord<UUID, ReplyDeliveryMessage>> =
        if (enabled) {
            val record = ProducerRecord<UUID, RequestDeliveryMessage>(requestTopic, UUID.randomUUID(), message)
            kafkaTemplate.sendAndReceive(record)
        } else {
            CompletableFuture.failedFuture(UnsupportedOperationException("ReplyingDeliveryKafkaProducer is disabled"))
        }
}