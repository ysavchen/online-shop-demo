package com.example.bookservice.api.kafka

import com.example.bookservice.mapping.BookMapper.toModel
import com.example.bookservice.repository.BookRepository
import com.example.bookservice.test.BookTestData.bookEntity
import com.example.bookservice.test.IntegrationTest
import com.example.bookservice.test.OrderTestData.order
import com.example.orderservice.domain.kafka.client.config.DomainOrderKafkaClientProperties
import com.example.orderservice.domain.kafka.client.model.DomainEvent
import com.example.orderservice.domain.kafka.client.model.OrderCreatedEvent
import com.example.orderservice.domain.kafka.client.model.Status
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.kafka.core.KafkaTemplate
import java.util.*
import kotlin.test.assertEquals

//todo: implement tests
@IntegrationTest
class OrderKafkaConsumerTests(
    @Autowired val bookRepository: BookRepository,
    @Autowired val testKafkaTemplate: KafkaTemplate<UUID, DomainEvent>,
    @Autowired val properties: DomainOrderKafkaClientProperties
) {

    val topic = properties.kafka.domain.consumer!!.topics.first()

    @Test
    fun `process created order`() {
        val book = bookRepository.save(bookEntity()).toModel()
        val order = order(book, Status.CREATED)

        val event = OrderCreatedEvent(order)
        testKafkaTemplate.send(topic, UUID.randomUUID(), event).get()

        Thread.sleep(500)
        val updatedBook = bookRepository.findByIdOrNull(book.id)!!.toModel()
        val expectedQuantity = book.quantity - order.items.first().quantity
        val actualQuantity = updatedBook.quantity
        assertEquals(expectedQuantity, actualQuantity)
    }

    @Test
    fun `duplicate message to process created order`() {
        val book = bookRepository.save(bookEntity()).toModel()
        val order = order(book, Status.CREATED)
        val messageKey = UUID.randomUUID()

        val event = OrderCreatedEvent(order)
        testKafkaTemplate.send(topic, messageKey, event).get()

        Thread.sleep(500)
        val updatedBook = bookRepository.findByIdOrNull(book.id)!!.toModel()
        val expectedQuantity = book.quantity - order.items.first().quantity
        val actualQuantity = updatedBook.quantity
        assertEquals(expectedQuantity, actualQuantity)

        testKafkaTemplate.send(topic, messageKey, event).get()

        Thread.sleep(500)
        val duplicateUpdateBook = bookRepository.findByIdOrNull(book.id)!!.toModel()
        val duplicateExpectedQuantity = updatedBook.quantity
        val duplicateActualQuantity = duplicateUpdateBook.quantity
        assertEquals(duplicateExpectedQuantity, duplicateActualQuantity)
    }

    @Test
    fun `process updated order`() {

    }

    @Test
    fun `duplicate message to process updated order`() {

    }
}