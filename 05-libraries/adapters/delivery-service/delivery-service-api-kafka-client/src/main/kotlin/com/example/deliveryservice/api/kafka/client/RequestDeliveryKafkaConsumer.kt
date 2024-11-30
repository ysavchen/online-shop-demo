package com.example.deliveryservice.api.kafka.client

import com.example.deliveryservice.api.kafka.client.model.RequestDeliveryMessage
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.listener.MessageListener
import java.util.*

interface RequestDeliveryKafkaConsumer : MessageListener<UUID, RequestDeliveryMessage> {

    override fun onMessage(data: ConsumerRecord<UUID, RequestDeliveryMessage>)
}