package com.example.orderservice.service.integration

import com.example.deliveryservice.kafka.client.model.RequestDeliveryMessage
import com.example.deliveryservice.request.kafka.client.RequestDeliveryKafkaProducer
import org.springframework.stereotype.Service

@Service
class DeliveryClientService(private val kafkaProducer: RequestDeliveryKafkaProducer) {

    fun send(message: RequestDeliveryMessage) = kafkaProducer.send(message)
}