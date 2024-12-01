package com.example.orderservice.service.integration

import com.example.deliveryservice.kafka.client.model.CreateDeliveryRequest
import com.example.deliveryservice.kafka.client.model.DeliveryCreatedResponse
import com.example.deliveryservice.request.kafka.client.ReplyingDeliveryKafkaProducer
import com.example.orderservice.api.rest.model.Delivery
import com.example.orderservice.api.rest.model.DeliveryRequest
import com.example.orderservice.mapping.api.DeliveryMapper.toModel
import com.example.orderservice.mapping.integration.DeliveryMapper.toKafkaModel
import org.springframework.stereotype.Service
import java.util.*

@Service
class DeliveryClientService(private val kafkaProducer: ReplyingDeliveryKafkaProducer) {

    fun createDelivery(orderId: UUID, request: DeliveryRequest): Delivery {
        val request = CreateDeliveryRequest(request.toKafkaModel(orderId))
        val future = kafkaProducer.sendAndReceive(request)
        val delivery = when (val response = future.get().value()) {
            is DeliveryCreatedResponse -> response.data.toModel()
        }
        return delivery
    }
}