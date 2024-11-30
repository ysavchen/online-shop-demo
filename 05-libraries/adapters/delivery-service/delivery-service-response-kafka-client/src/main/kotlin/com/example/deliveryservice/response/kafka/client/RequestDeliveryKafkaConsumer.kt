package com.example.deliveryservice.response.kafka.client

import com.example.deliveryservice.kafka.client.model.RequestDeliveryMessage
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.listener.MessageListener
import java.util.*

interface RequestDeliveryKafkaConsumer : MessageListener<UUID, RequestDeliveryMessage> {

    override fun onMessage(data: ConsumerRecord<UUID, RequestDeliveryMessage>)
}