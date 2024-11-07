package com.example.orderservice.service.integration

import com.example.orderservice.domain.kafka.client.DomainOrderKafkaProducer
import com.example.orderservice.domain.kafka.client.model.DomainEvent
import org.springframework.stereotype.Service

@Service
class DomainEventService(private val kafkaProducer: DomainOrderKafkaProducer) {

    fun send(event: DomainEvent) = kafkaProducer.send(event)
}