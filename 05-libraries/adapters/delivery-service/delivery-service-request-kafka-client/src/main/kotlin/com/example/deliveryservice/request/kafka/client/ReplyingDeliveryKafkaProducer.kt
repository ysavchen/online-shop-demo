package com.example.deliveryservice.request.kafka.client

import com.example.deliveryservice.kafka.client.model.RequestDeliveryMessage
import com.example.deliveryservice.kafka.client.model.ResponseDeliveryMessage
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate
import java.util.*
import java.util.concurrent.CompletableFuture

interface ReplyingDeliveryKafkaProducer {

    fun sendAndReceive(message: RequestDeliveryMessage): CompletableFuture<ConsumerRecord<UUID, ResponseDeliveryMessage>>
}

class ReplyingDeliveryKafkaProducerImpl(
    private val enabled: Boolean,
    private val requestTopic: String,
    private val kafkaTemplate: ReplyingKafkaTemplate<UUID, RequestDeliveryMessage, ResponseDeliveryMessage>
) : ReplyingDeliveryKafkaProducer {

    override fun sendAndReceive(message: RequestDeliveryMessage): CompletableFuture<ConsumerRecord<UUID, ResponseDeliveryMessage>> =
        if (enabled) {
            val record = ProducerRecord<UUID, RequestDeliveryMessage>(requestTopic, UUID.randomUUID(), message)
            kafkaTemplate.sendAndReceive(record)
        } else {
            CompletableFuture.failedFuture(UnsupportedOperationException("RequestDeliveryKafkaProducer is disabled"))
        }
}