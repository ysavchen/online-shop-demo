package com.example.orderservice.domain.kafka.client

import com.example.orderservice.domain.kafka.client.model.DomainEvent
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import java.util.*
import java.util.concurrent.CompletableFuture

interface DomainOrderKafkaProducer {

    fun send(event: DomainEvent): CompletableFuture<SendResult<UUID, DomainEvent>>
}

class DomainOrderKafkaProducerImpl(
    private val kafkaTemplate: KafkaTemplate<UUID, DomainEvent>
) : DomainOrderKafkaProducer {

    override fun send(event: DomainEvent): CompletableFuture<SendResult<UUID, DomainEvent>> =
        kafkaTemplate.sendDefault(UUID.randomUUID(), event)
}