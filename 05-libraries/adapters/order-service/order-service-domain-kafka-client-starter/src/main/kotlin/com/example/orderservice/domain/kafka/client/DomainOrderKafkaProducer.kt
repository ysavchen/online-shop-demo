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
    private val kafkaTemplate: KafkaTemplate<UUID, DomainEvent>,
    private val enabled: Boolean
) : DomainOrderKafkaProducer {

    override fun send(event: DomainEvent): CompletableFuture<SendResult<UUID, DomainEvent>> =
        if (enabled) {
            kafkaTemplate.sendDefault(UUID.randomUUID(), event)
        } else {
            CompletableFuture.failedFuture(UnsupportedOperationException("DomainOrderKafkaProducer is disabled"))
        }
}