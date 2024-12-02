package com.example.bookservice.api.kafka

import com.example.bookservice.service.BookService
import com.example.orderservice.domain.kafka.client.DomainOrderKafkaConsumer
import com.example.orderservice.domain.kafka.client.model.DomainEvent
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.stereotype.Component
import java.util.*

@Component
class OrderKafkaConsumer(private val bookService: BookService) : DomainOrderKafkaConsumer {

    override fun onMessage(data: ConsumerRecord<UUID, DomainEvent>) {
        bookService.processMessage(data)
    }
}