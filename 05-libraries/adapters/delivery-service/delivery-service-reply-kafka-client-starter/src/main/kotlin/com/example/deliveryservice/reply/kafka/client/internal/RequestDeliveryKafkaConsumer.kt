package com.example.deliveryservice.reply.kafka.client.internal

import com.example.deliveryservice.kafka.client.model.RequestDeliveryMessage
import com.example.deliveryservice.reply.kafka.client.ReplyingDeliveryKafkaConsumer
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.listener.MessageListener
import org.springframework.kafka.support.KafkaHeaders
import java.util.*

class RequestDeliveryKafkaConsumer(
    private val enabled: Boolean,
    private val kafkaConsumer: ReplyingDeliveryKafkaConsumer,
    private val kafkaProducer: ReplyDeliveryKafkaProducer
) : MessageListener<UUID, RequestDeliveryMessage> {

    override fun onMessage(data: ConsumerRecord<UUID, RequestDeliveryMessage>) {
        if (enabled) {
            val replyDeliveryMessage = kafkaConsumer.onMessage(data)
            val correlationId = data.headers().lastHeader(KafkaHeaders.CORRELATION_ID)
            kafkaProducer.send(correlationId, replyDeliveryMessage)
        }
    }
}