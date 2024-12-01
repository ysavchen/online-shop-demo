package com.example.deliveryservice.api.kafka

import com.example.deliveryservice.kafka.client.model.ReplyDeliveryMessage
import com.example.deliveryservice.kafka.client.model.RequestDeliveryMessage
import com.example.deliveryservice.response.kafka.client.ReplyingDeliveryKafkaConsumer
import com.example.deliveryservice.service.DeliveryService
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.stereotype.Component
import java.util.*

@Component
class ReplyingDeliveryKafkaConsumerImpl(
    private val deliveryService: DeliveryService
) : ReplyingDeliveryKafkaConsumer {

    override fun onMessage(
        data: ConsumerRecord<UUID, RequestDeliveryMessage>
    ): ReplyDeliveryMessage = deliveryService.processMessage(data)

}