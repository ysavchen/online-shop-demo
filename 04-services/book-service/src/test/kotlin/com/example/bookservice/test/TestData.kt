package com.example.bookservice.test

import com.example.bookservice.api.rest.model.*
import com.example.bookservice.repository.entity.*
import com.example.online.shop.model.*
import com.example.orderservice.domain.kafka.client.model.*
import org.apache.commons.lang3.RandomStringUtils
import java.time.OffsetDateTime
import java.util.*

typealias RestCurrency = com.example.bookservice.api.rest.model.Currency
typealias KafkaCurrency = com.example.orderservice.domain.kafka.client.model.Currency

private val randomString = RandomStringUtils.insecure()

object BookTestData {

    fun createBookRequest() = CreateBookRequest(
        isbn = Isbn("978-1-52582-668-9"),
        title = Title(randomString.nextAlphabetic(15)),
        authors = listOf(Author(randomString.nextAlphabetic(10))),
        description = Description(randomString.nextAlphanumeric(25)),
        genre = nextValue<Genre>(),
        releaseDate = randomLocalDate(),
        quantity = Quantity(randomString.nextNumeric(3).toInt()),
        price = Price(PriceValue(randomPrice()), nextValue<RestCurrency>())
    )

    fun updateBookRequest() = UpdateBookRequest(
        releaseDate = randomLocalDate(),
        quantity = Quantity(randomString.nextNumeric(3).toInt()),
        price = Price(PriceValue(randomPrice()), nextValue<RestCurrency>())
    )

    fun bookEntity(
        isbn: Isbn = Isbn("978-1-52582-668-9")
    ) = BookEntity(
        isbn = isbn,
        title = Title(randomString.nextAlphabetic(15)),
        authors = arrayOf(randomString.nextAlphabetic(10)),
        description = Description(randomString.nextAlphanumeric(25)),
        genre = nextValue<GenreEntity>(),
        releaseDate = randomLocalDate(),
        quantity = Quantity(randomString.nextNumeric(3).toInt()),
        price = PriceEntity(randomPrice(), nextValue<CurrencyEntity>())
    )
}

object ReviewTestData {

    fun createReviewRequest(bookId: UUID) = CreateReviewRequest(
        title = Title(randomString.nextAlphabetic(15)),
        reviewText = ReviewText(randomString.nextAlphabetic(25)),
        author = Author(randomString.nextAlphabetic(10)),
        rating = Rating(randomRating()),
        bookId = bookId
    )

    fun reviewEntity(bookFk: UUID) = ReviewEntity(
        title = Title(randomString.nextAlphabetic(15)),
        reviewText = ReviewText(randomString.nextAlphabetic(25)),
        author = Author(randomString.nextAlphabetic(10)),
        rating = Rating(randomRating()),
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
        totalQuantity = Quantity(orderItems.sumOf { it.quantity.value }),
        totalPrice = TotalPrice(
            value = PriceValue(orderItems.sumOf { it.price.value.value }),
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
        quantity = Quantity(randomString.nextNumeric(1).toInt()),
        price = ItemPrice(
            value = book.price!!.value,
            currency = ItemCurrency.RUB
        )
    )
}