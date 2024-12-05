package com.example.orderservice.service.integration

import com.example.deliveryservice.kafka.client.model.*
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
        val request = request.toKafkaModel(orderId)
        val reply = kafkaProducer.sendAndReceive(request).get().value()
        val delivery = when (reply) {
            is DeliveryDataReply -> reply.data.toModel()
            is DuplicateMessageErrorReply -> recover(reply)
            else -> throw exception(reply)
        }
        return delivery
    }

    fun deliveryById(deliveryId: UUID): Delivery {
        val request = GetDeliveryByIdRequest(deliveryId)
        val reply = kafkaProducer.sendAndReceive(request).get().value()
        val delivery = when (reply) {
            is DeliveryDataReply -> reply.data.toModel()
            else -> throw exception(reply)
        }
        return delivery
    }

    fun deliveryByOrderId(orderId: UUID): Delivery {
        val request = GetDeliveryByOrderIdRequest(orderId)
        val reply = kafkaProducer.sendAndReceive(request).get().value()
        val delivery = when (reply) {
            is DeliveryDataReply -> reply.data.toModel()
            else -> throw exception(reply)
        }
        return delivery
    }

    private fun recover(reply: DuplicateMessageErrorReply): Delivery {
        val id = reply.data.details.resourceId
        return deliveryById(id)
    }

    private fun exception(message: ReplyDeliveryMessage): DownstreamServiceException =
        DownstreamServiceException(message.data.toString(), message.meta.service)

}