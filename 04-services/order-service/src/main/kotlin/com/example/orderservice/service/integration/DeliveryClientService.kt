package com.example.orderservice.service.integration

import com.example.deliveryservice.kafka.client.model.ClientErrorReply
import com.example.deliveryservice.kafka.client.model.CreateDeliveryRequest
import com.example.deliveryservice.kafka.client.model.DeliveryCreatedReply
import com.example.deliveryservice.request.kafka.client.ReplyingDeliveryKafkaProducer
import com.example.orderservice.api.rest.DownstreamServiceException
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
        val reply = kafkaProducer.sendAndReceive(request).get().value()
        val delivery = when (reply) {
            is DeliveryCreatedReply -> reply.data.toModel()
            is ClientErrorReply -> throw DownstreamServiceException(
                "Error message: ${reply.data.message}, " +
                        "errorCode=${reply.data.errorCode}, service=${reply.meta.service}"
            )
        }
        return delivery
    }
}