package com.example.deliveryservice.response.kafka.client

import com.example.deliveryservice.kafka.client.model.RequestDeliveryMessage
import com.example.deliveryservice.kafka.client.model.ResponseDeliveryMessage
import org.apache.kafka.clients.consumer.ConsumerRecord
import java.util.*

interface ReplyingDeliveryKafkaConsumer {

    fun onMessage(data: ConsumerRecord<UUID, RequestDeliveryMessage>): ResponseDeliveryMessage
}