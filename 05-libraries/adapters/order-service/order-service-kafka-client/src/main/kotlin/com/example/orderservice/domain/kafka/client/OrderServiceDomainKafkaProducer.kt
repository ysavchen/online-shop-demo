package com.example.orderservice.domain.kafka.client

import com.example.orderservice.domain.kafka.client.model.Order
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import java.util.*
import java.util.concurrent.CompletableFuture

interface OrderServiceDomainKafkaProducer {

    fun send(data: Order): CompletableFuture<SendResult<UUID, Order>>
}

class OrderServiceDomainKafkaProducerImpl(
    private val kafkaTemplate: KafkaTemplate<UUID, Order>
) : OrderServiceDomainKafkaProducer {

    override fun send(data: Order): CompletableFuture<SendResult<UUID, Order>> =
        kafkaTemplate.sendDefault(UUID.randomUUID(), data)
}