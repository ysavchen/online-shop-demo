package com.example.deliveryservice.response.kafka.client

import com.example.deliveryservice.kafka.client.model.RequestDeliveryMessage
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.listener.MessageListener
import org.springframework.kafka.support.KafkaHeaders
import java.util.*

interface RequestDeliveryKafkaConsumer : MessageListener<UUID, RequestDeliveryMessage> {

    override fun onMessage(data: ConsumerRecord<UUID, RequestDeliveryMessage>)
}

class RequestDeliveryKafkaConsumerImpl(
    private val enabled: Boolean,
    private val replyingDeliveryKafkaConsumer: ReplyingDeliveryKafkaConsumer,
    private val responseDeliveryKafkaProducer: ResponseDeliveryKafkaProducer
) : RequestDeliveryKafkaConsumer {

    override fun onMessage(data: ConsumerRecord<UUID, RequestDeliveryMessage>) {
        if (enabled) {
            val responseDeliveryMessage = replyingDeliveryKafkaConsumer.onMessage(data)
            val correlationId = data.headers().lastHeader(KafkaHeaders.CORRELATION_ID)
            responseDeliveryKafkaProducer.send(correlationId, responseDeliveryMessage)
        }
    }
}