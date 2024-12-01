package com.example.deliveryservice.reply.kafka.client

import com.example.deliveryservice.kafka.client.model.ReplyDeliveryMessage
import com.example.deliveryservice.kafka.client.model.RequestDeliveryMessage
import org.apache.kafka.clients.consumer.ConsumerRecord
import java.util.*

interface ReplyingDeliveryKafkaConsumer {

    fun onMessage(data: ConsumerRecord<UUID, RequestDeliveryMessage>): ReplyDeliveryMessage
}