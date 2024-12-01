package com.example.deliveryservice.response.kafka.client

import com.example.deliveryservice.kafka.client.model.RequestDeliveryMessage
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.listener.MessageListener
import org.springframework.kafka.support.KafkaHeaders.CORRELATION_ID
import java.util.*

class RequestDeliveryKafkaConsumer(
    private val enabled: Boolean,
    private val kafkaConsumer: ReplyingDeliveryKafkaConsumer,
    private val kafkaProducer: ReplyDeliveryKafkaProducer
) : MessageListener<UUID, RequestDeliveryMessage> {

    override fun onMessage(data: ConsumerRecord<UUID, RequestDeliveryMessage>) {
        if (enabled) {
            val replyDeliveryMessage = kafkaConsumer.onMessage(data)
            val correlationId = data.headers().lastHeader(CORRELATION_ID)
            kafkaProducer.send(correlationId, replyDeliveryMessage)
        }
    }
}