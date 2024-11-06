package com.example.orderservice.service

import com.example.orderservice.domain.kafka.client.OrderServiceDomainKafkaProducer
import com.example.orderservice.domain.kafka.client.model.DomainEvent
import org.springframework.stereotype.Service

@Service
class DomainEventService(private val kafkaProducer: OrderServiceDomainKafkaProducer) {

    fun send(event: DomainEvent) = kafkaProducer.send(event)

}