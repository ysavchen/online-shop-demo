package com.example.bookservice.test

import com.example.bookservice.api.rest.model.*
import com.example.bookservice.repository.entity.*
import com.example.online.shop.model.Description
import com.example.online.shop.model.Isbn
import com.example.online.shop.model.Title
import com.example.orderservice.domain.kafka.client.model.*
import org.apache.commons.lang3.RandomStringUtils.*
import java.time.OffsetDateTime
import java.util.*

typealias RestCurrency = com.example.bookservice.api.rest.model.Currency
typealias KafkaCurrency = com.example.orderservice.domain.kafka.client.model.Currency

object BookTestData {

    fun createBookRequest() = CreateBookRequest(
        isbn = Isbn.valueOf("9781525826689"),
        title = Title.valueOf(randomAlphabetic(15)),
        authors = listOf(randomAlphabetic(10)),
        description = Description.valueOf(randomAlphanumeric(25)),
        genre = nextValue<Genre>(),
        releaseDate = randomLocalDate(),
        quantity = randomNumeric(3).toInt(),
        price = Price(randomPrice(), nextValue<RestCurrency>())
    )

    fun updateBookRequest() = UpdateBookRequest(
        releaseDate = randomLocalDate(),
        quantity = randomNumeric(3).toInt(),
        price = Price(randomPrice(), nextValue<RestCurrency>())
    )

    fun bookEntity(
        isbn: Isbn = Isbn.valueOf("9781525826689")
    ) = BookEntity(
        isbn = isbn,
        title = Title.valueOf(randomAlphabetic(15)),
        authors = arrayOf(randomAlphabetic(10)),
        description = Description.valueOf(randomAlphanumeric(25)),
        genre = nextValue<GenreEntity>(),
        releaseDate = randomLocalDate(),
        quantity = randomNumeric(3).toInt(),
        price = PriceEntity(randomPrice(), nextValue<CurrencyEntity>())
    )
}

object ReviewTestData {

    fun createReviewRequest(bookId: UUID) = CreateReviewRequest(
        title = Title.valueOf(randomAlphabetic(15)),
        reviewText = randomAlphabetic(25),
        author = randomAlphabetic(10),
        rating = randomRating(),
        bookId = bookId
    )

    fun reviewEntity(bookFk: UUID) = ReviewEntity(
        title = Title.valueOf(randomAlphabetic(15)),
        reviewText = randomAlphabetic(25),
        author = randomAlphabetic(10),
        rating = randomRating(),
        bookFk = bookFk
    )
}

object OrderTestData {

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
            currency = nextValue<KafkaCurrency>()
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
            currency = ItemCurrency.RUB
        )
    )
}