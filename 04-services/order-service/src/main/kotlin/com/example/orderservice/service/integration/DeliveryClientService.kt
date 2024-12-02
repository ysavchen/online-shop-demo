package com.example.orderservice.service.integration

import com.example.deliveryservice.kafka.client.model.*
import com.example.deliveryservice.kafka.client.model.ErrorCode.MESSAGE_ALREADY_PROCESSED
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
            is DeliveryDataReply -> reply.data.toModel()
            is ClientErrorReply ->
                if (reply.data.errorCode == MESSAGE_ALREADY_PROCESSED) {
                    recover(reply)
                } else throw exception(reply)
        }
        return delivery
    }

    fun deliveryById(id: UUID): Delivery {
        val request = GetDeliveryByIdRequest(GetDeliveryById(id))
        val reply = kafkaProducer.sendAndReceive(request).get().value()
        val delivery = when (reply) {
            is DeliveryDataReply -> reply.data.toModel()
            is ClientErrorReply -> throw exception(reply)
        }
        return delivery
    }

    private fun recover(reply: ClientErrorReply): Delivery {
        val details = reply.data.details as DuplicateMessageDetails
        return deliveryById(details.resourceId)
    }

    private fun exception(reply: ClientErrorReply) = DownstreamServiceException(
        message = reply.data.message,
        service = reply.meta.service
    )
}