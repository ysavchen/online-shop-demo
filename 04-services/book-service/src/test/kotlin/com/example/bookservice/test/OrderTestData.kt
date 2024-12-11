package com.example.bookservice.test

import com.example.bookservice.api.rest.model.Book
import com.example.orderservice.domain.kafka.client.model.*
import com.example.orderservice.domain.kafka.client.model.Currency
import org.apache.commons.lang3.RandomStringUtils.randomNumeric
import java.time.OffsetDateTime
import java.util.*

object OrderTestData {

    fun orderCreatedEvent(book: Book) = OrderCreatedEvent(
        data = order(book)
    )

    fun orderUpdatedEvent(book: Book) = OrderUpdatedEvent(
        data = order(book)
    )

    fun order(
        book: Book,
        status: Status = nextValue<Status>(),
        orderItems: Set<OrderItem> = setOf(orderItem(book))
    ) = Order(
        id = UUID.randomUUID(),
        userId = UUID.randomUUID(),
        status = status,
        items = orderItems,
        totalQuantity = orderItems.sumOf { it.quantity },
        totalPrice = TotalPrice(
            value = orderItems.sumOf { it.price.value },
            currency = nextValue<Currency>()
        ),
        createdAt = OffsetDateTime.now(),
        updatedAt = OffsetDateTime.now()
    )

    fun orderItem(
        book: Book
    ) = OrderItem(
        id = book.id,
        category = ItemCategory.BOOKS,
        quantity = randomNumeric(1).toInt(),
        price = ItemPrice(
            value = book.price!!.value,
            currency = ItemCurrency.valueOf(book.price.currency.name)
        )
    )
}