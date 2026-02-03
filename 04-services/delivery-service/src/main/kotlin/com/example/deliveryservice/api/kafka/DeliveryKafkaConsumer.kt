package com.example.deliveryservice.api.kafka

import com.example.deliveryservice.kafka.client.model.*
import com.example.deliveryservice.reply.kafka.client.ReplyingDeliveryKafkaConsumer
import com.example.deliveryservice.service.DeliveryService
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.stereotype.Component
import java.util.*

@Component
class DeliveryKafkaConsumer(private val deliveryService: DeliveryService) : ReplyingDeliveryKafkaConsumer {

    override fun onMessage(data: ConsumerRecord<UUID, RequestDeliveryMessage>): ReplyDeliveryMessage =
        when (val request = data.value()) {
            is GetDeliveryByIdRequest -> deliveryService.getDeliveryById(request.data.deliveryId)
            is GetDeliveryByOrderIdRequest -> deliveryService.getDeliveryByOrderId(request.data.orderId)
            is CreateDeliveryRequest -> deliveryService.createDelivery(data.key(), request)
        }
}