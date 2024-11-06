package com.example.orderservice.domain.kafka.client

import com.example.orderservice.domain.kafka.client.model.DomainEvent
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.listener.MessageListener
import java.util.*

interface OrderServiceDomainKafkaConsumer : MessageListener<UUID, DomainEvent> {

    override fun onMessage(data: ConsumerRecord<UUID, DomainEvent>)
}