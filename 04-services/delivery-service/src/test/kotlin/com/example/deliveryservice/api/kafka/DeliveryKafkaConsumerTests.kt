package com.example.deliveryservice.api.kafka;

import com.example.deliveryservice.kafka.client.model.*
import com.example.deliveryservice.mapping.DeliveryMapper.toModel
import com.example.deliveryservice.reply.kafka.client.autoconfigure.ReplyDeliveryKafkaClientProperties
import com.example.deliveryservice.repository.DeliveryRepository
import com.example.deliveryservice.test.DeliveryTestData.createDeliveryRequest
import com.example.deliveryservice.test.DeliveryTestData.deliveryEntity
import com.example.deliveryservice.test.IntegrationTest
import org.apache.kafka.clients.producer.ProducerRecord
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate
import java.util.*
import kotlin.test.assertEquals

@Disabled
@IntegrationTest
class DeliveryKafkaConsumerTests(
    @Autowired val properties: ReplyDeliveryKafkaClientProperties,
    @Autowired val deliveryRepository: DeliveryRepository,
    @Autowired val testKafkaTemplate: ReplyingKafkaTemplate<UUID, RequestDeliveryMessage, ReplyDeliveryMessage>
) {

    val requestTopic: String = properties.kafka.replying.consumer.request.topics.first()

    @BeforeEach
    fun beforeEach() {
        deliveryRepository.deleteAll()
    }

    @Test
    fun `get delivery by id`() {
        val delivery = deliveryRepository.save(deliveryEntity()).toModel()

        val request = GetDeliveryByIdRequest(delivery.id)
        val record = ProducerRecord<UUID, RequestDeliveryMessage>(requestTopic, UUID.randomUUID(), request)
        val reply = testKafkaTemplate.sendAndReceive(record).get().value()

        val deliveryData = reply.data as Delivery
        assertEquals(delivery, deliveryData)

        val messageMeta = reply.meta
        assertEquals(StatusCode.SUCCESS, messageMeta.statusCode)
    }

    @Test
    fun `get delivery by non-existing id`() {
        val nonExistingId = UUID.randomUUID()

        val request = GetDeliveryByIdRequest(nonExistingId)
        val record = ProducerRecord<UUID, RequestDeliveryMessage>(requestTopic, UUID.randomUUID(), request)
        val reply = testKafkaTemplate.sendAndReceive(record).get().value()

        val notFoundError = reply.data as DeliveryNotFoundError
        assertEquals(ErrorCode.RESOURCE_NOT_FOUND, notFoundError.errorCode)

        val messageMeta = reply.meta
        assertEquals(StatusCode.ERROR, messageMeta.statusCode)
        assertEquals(MessageType.DELIVERY_NOT_FOUND_ERROR_REPLY, messageMeta.type)
    }

    @Test
    fun `get delivery by order id`() {
        val delivery = deliveryRepository.save(deliveryEntity()).toModel()

        val request = GetDeliveryByOrderIdRequest(delivery.orderId)
        val record = ProducerRecord<UUID, RequestDeliveryMessage>(requestTopic, UUID.randomUUID(), request)
        val reply = testKafkaTemplate.sendAndReceive(record).get().value()

        val deliveryData = reply.data as Delivery
        assertEquals(delivery, deliveryData)

        val messageMeta = reply.meta
        assertEquals(StatusCode.SUCCESS, messageMeta.statusCode)
    }

    @Test
    fun `get delivery by non-existing order id`() {
        val nonExistingId = UUID.randomUUID()

        val request = GetDeliveryByOrderIdRequest(nonExistingId)
        val record = ProducerRecord<UUID, RequestDeliveryMessage>(requestTopic, UUID.randomUUID(), request)
        val reply = testKafkaTemplate.sendAndReceive(record).get().value()

        val notFoundError = reply.data as DeliveryNotFoundError
        assertEquals(ErrorCode.RESOURCE_NOT_FOUND, notFoundError.errorCode)

        val messageMeta = reply.meta
        assertEquals(StatusCode.ERROR, messageMeta.statusCode)
        assertEquals(MessageType.DELIVERY_NOT_FOUND_ERROR_REPLY, messageMeta.type)
    }

    @Test
    fun `create delivery`() {
        val request = createDeliveryRequest()
        val record = ProducerRecord<UUID, RequestDeliveryMessage>(requestTopic, UUID.randomUUID(), request)
        val reply = testKafkaTemplate.sendAndReceive(record).get().value()

        val deliveryData = reply.data as Delivery
        assertThat(deliveryData)
            .hasFieldOrProperty("id")
            .hasFieldOrProperty("date")
            .hasFieldOrPropertyWithValue("type", request.data.type)
            .hasFieldOrPropertyWithValue("address", request.data.address)
            .hasFieldOrPropertyWithValue("status", Status.CREATED)
            .hasFieldOrPropertyWithValue("orderId", request.data.orderId)

        val messageMeta = reply.meta
        assertEquals(StatusCode.SUCCESS, messageMeta.statusCode)
    }

    @Test
    fun `duplicate create delivery request`() {
        val request = createDeliveryRequest()
        val messageKey = UUID.randomUUID()

        val record = ProducerRecord<UUID, RequestDeliveryMessage>(requestTopic, messageKey, request)
        val reply = testKafkaTemplate.sendAndReceive(record).get().value()

        val deliveryData = reply.data as Delivery
        assertThat(deliveryData).hasFieldOrProperty("id")
        val messageMeta = reply.meta
        assertEquals(StatusCode.SUCCESS, messageMeta.statusCode)

        val duplicateRecord = ProducerRecord<UUID, RequestDeliveryMessage>(requestTopic, messageKey, request)
        val duplicateReply = testKafkaTemplate.sendAndReceive(duplicateRecord).get().value()

        val duplicateMessageError = duplicateReply.data as DuplicateMessageError
        assertEquals(ErrorCode.DUPLICATE_MESSAGE_ERROR, duplicateMessageError.errorCode)

        val duplicateMessageMeta = duplicateReply.meta
        assertEquals(StatusCode.ERROR, duplicateMessageMeta.statusCode)
        assertEquals(MessageType.DUPLICATE_MESSAGE_ERROR_REPLY, duplicateMessageMeta.type)
    }
}