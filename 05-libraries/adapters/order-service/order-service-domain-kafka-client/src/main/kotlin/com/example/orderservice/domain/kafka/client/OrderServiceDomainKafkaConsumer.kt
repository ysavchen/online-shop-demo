package com.example.orderservice.domain.kafka.client

import com.example.orderservice.domain.kafka.client.model.Order
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.listener.MessageListener
import java.util.*

interface OrderServiceDomainKafkaConsumer : MessageListener<UUID, Order> {

    override fun onMessage(data: ConsumerRecord<UUID, Order>)
}