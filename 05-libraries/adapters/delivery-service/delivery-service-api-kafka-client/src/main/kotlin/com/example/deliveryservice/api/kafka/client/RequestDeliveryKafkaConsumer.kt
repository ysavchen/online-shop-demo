package com.example.deliveryservice.api.kafka.client

import com.example.deliveryservice.api.kafka.client.model.RequestMessage
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.listener.MessageListener
import java.util.*

interface RequestDeliveryKafkaConsumer : MessageListener<UUID, RequestMessage> {

    override fun onMessage(data: ConsumerRecord<UUID, RequestMessage>)
}