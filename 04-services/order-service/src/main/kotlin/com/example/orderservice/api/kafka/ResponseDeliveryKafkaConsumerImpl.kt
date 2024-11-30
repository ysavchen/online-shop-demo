package com.example.orderservice.api.kafka

import com.example.deliveryservice.kafka.client.model.ResponseDeliveryMessage
import com.example.deliveryservice.request.kafka.client.ResponseDeliveryKafkaConsumer
import com.example.orderservice.service.OrderService
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.stereotype.Component
import java.util.*

@Component
class ResponseDeliveryKafkaConsumerImpl(
    private val orderService: OrderService
) : ResponseDeliveryKafkaConsumer {

    override fun onMessage(data: ConsumerRecord<UUID, ResponseDeliveryMessage>) {
        orderService.processMessage(data)
    }
}