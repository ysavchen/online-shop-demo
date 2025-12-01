package com.example.bookservice.test

import com.example.bookservice.api.rest.model.*
import com.example.bookservice.repository.entity.*
import com.example.online.shop.model.Isbn
import com.example.online.shop.model.PriceValue
import com.example.online.shop.model.Quantity
import com.example.online.shop.model.test.ModelTestData.author
import com.example.online.shop.model.test.ModelTestData.description
import com.example.online.shop.model.test.ModelTestData.priceValue
import com.example.online.shop.model.test.ModelTestData.quantity
import com.example.online.shop.model.test.ModelTestData.rating
import com.example.online.shop.model.test.ModelTestData.reviewText
import com.example.online.shop.model.test.ModelTestData.title
import com.example.orderservice.domain.kafka.client.model.*
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.*

typealias RestCurrency = com.example.bookservice.api.rest.model.Currency
typealias KafkaCurrency = com.example.orderservice.domain.kafka.client.model.Currency

object BookTestData {

    fun createBookRequest() = CreateBookRequest(
        isbn = Isbn("9781525826689"),
        title = title(15),
        authors = listOf(author(10)),
        description = description(25),
        genre = nextValue<Genre>(),
        releaseDate = randomLocalDate(),
        quantity = quantity(3),
        price = Price(priceValue(BigDecimal.valueOf(250)), nextValue<RestCurrency>())
    )

    fun updateBookRequest() = UpdateBookRequest(
        releaseDate = randomLocalDate(),
        quantity = quantity(3),
        price = Price(priceValue(BigDecimal.valueOf(250)), nextValue<RestCurrency>())
    )

    fun bookEntity(
        isbn: Isbn = Isbn("9781525826689")
    ) = BookEntity(
        isbn = isbn,
        title = title(15),
        authors = arrayOf(author(10).value),
        description = description(25),
        genre = nextValue<GenreEntity>(),
        releaseDate = randomLocalDate(),
        quantity = quantity(3),
        price = PriceEntity(priceValue(BigDecimal.valueOf(250)).value, nextValue<CurrencyEntity>())
    )
}

object ReviewTestData {

    fun createReviewRequest(bookId: UUID) = CreateReviewRequest(
        title = title(15),
        reviewText = reviewText(25),
        author = author(10),
        rating = rating(BigDecimal.valueOf(5)),
        bookId = bookId
    )

    fun reviewEntity(bookFk: UUID) = ReviewEntity(
        title = title(15),
        reviewText = reviewText(25),
        author = author(10),
        rating = rating(BigDecimal.valueOf(5)),
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
        quantity = quantity(3),
        price = ItemPrice(
            value = book.price!!.value,
            currency = ItemCurrency.RUB
        )
    )
}