package com.example.deliveryservice.reply.kafka.client.internal

import com.example.deliveryservice.kafka.client.model.ReplyDeliveryMessage
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.header.Header
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import java.util.UUID
import java.util.concurrent.CompletableFuture

class ReplyDeliveryKafkaProducer(
    private val replyTopic: String,
    private val kafkaTemplate: KafkaTemplate<UUID, ReplyDeliveryMessage>
) {

    fun send(correlationId: Header?, message: ReplyDeliveryMessage): CompletableFuture<SendResult<UUID, ReplyDeliveryMessage>> {
        val record = ProducerRecord<UUID, ReplyDeliveryMessage>(replyTopic, UUID.randomUUID(), message).apply {
            correlationId?.let { headers().add(it) }
        }
        return kafkaTemplate.send(record)
    }
}