package com.example.deliveryservice.response.kafka.client

import com.example.deliveryservice.kafka.client.model.RequestDeliveryMessage
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.listener.MessageListener
import org.springframework.kafka.support.KafkaHeaders.CORRELATION_ID
import java.util.*

interface RequestDeliveryKafkaConsumer : MessageListener<UUID, RequestDeliveryMessage> {

    override fun onMessage(data: ConsumerRecord<UUID, RequestDeliveryMessage>)
}

class RequestDeliveryKafkaConsumerImpl(
    private val enabled: Boolean,
    private val kafkaConsumer: ReplyingDeliveryKafkaConsumer,
    private val kafkaProducer: ResponseDeliveryKafkaProducer
) : RequestDeliveryKafkaConsumer {

    override fun onMessage(data: ConsumerRecord<UUID, RequestDeliveryMessage>) {
        if (enabled) {
            val responseDeliveryMessage = kafkaConsumer.onMessage(data)
            val correlationId = data.headers().lastHeader(CORRELATION_ID)
            kafkaProducer.send(correlationId, responseDeliveryMessage)
        }
    }
}