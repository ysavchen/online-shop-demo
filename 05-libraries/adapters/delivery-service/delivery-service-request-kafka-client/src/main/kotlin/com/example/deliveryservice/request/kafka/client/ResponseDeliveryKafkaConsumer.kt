package com.example.deliveryservice.request.kafka.client

import com.example.deliveryservice.kafka.client.model.ResponseDeliveryMessage
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.listener.MessageListener
import java.util.*

interface ResponseDeliveryKafkaConsumer : MessageListener<UUID, ResponseDeliveryMessage> {

    override fun onMessage(data: ConsumerRecord<UUID, ResponseDeliveryMessage>)
}