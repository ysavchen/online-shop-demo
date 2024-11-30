package com.example.deliveryservice.api.kafka

import com.example.deliveryservice.kafka.client.model.RequestDeliveryMessage
import com.example.deliveryservice.response.kafka.client.RequestDeliveryKafkaConsumer
import com.example.deliveryservice.service.DeliveryService
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.stereotype.Component
import java.util.*

@Component
class RequestDeliveryKafkaConsumerImpl(
    private val deliveryService: DeliveryService
) : RequestDeliveryKafkaConsumer {

    override fun onMessage(data: ConsumerRecord<UUID, RequestDeliveryMessage>) {
        deliveryService.processMessage(data)
    }
}