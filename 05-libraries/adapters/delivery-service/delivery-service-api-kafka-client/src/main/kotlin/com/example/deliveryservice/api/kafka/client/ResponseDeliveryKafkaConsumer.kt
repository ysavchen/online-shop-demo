package com.example.deliveryservice.api.kafka.client

import com.example.deliveryservice.api.kafka.client.model.ResponseMessage
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.listener.MessageListener
import java.util.*

interface ResponseDeliveryKafkaConsumer : MessageListener<UUID, ResponseMessage> {

    override fun onMessage(data: ConsumerRecord<UUID, ResponseMessage>)
}