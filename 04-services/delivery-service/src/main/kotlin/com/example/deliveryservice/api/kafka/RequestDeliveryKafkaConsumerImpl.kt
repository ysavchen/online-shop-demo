package com.example.deliveryservice.api.kafka

import com.example.deliveryservice.service.DeliveryService
import org.springframework.stereotype.Component

@Component
class RequestDeliveryKafkaConsumerImpl(
    private val deliveryService: DeliveryService
) {
}