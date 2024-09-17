package com.example.bookservice.test

import com.example.bookservice.api.rest.model.*
import com.example.bookservice.api.rest.model.Currency
import com.example.bookservice.repository.entity.*
import org.apache.commons.lang3.RandomStringUtils.*
import java.util.*

object BookTestData {

    fun createBookRequest() = CreateBookRequest(
        isbn = randomNumeric(17),
        title = randomAlphabetic(15),
        authors = listOf(randomAlphabetic(10)),
        description = randomAlphanumeric(25),
        genre = nextValue<Genre>(),
        releaseDate = randomLocalDate(),
        quantity = randomNumeric(3).toInt(),
        price = Price(randomPrice(), nextValue<Currency>())
    )

    fun updateBookRequest() = UpdateBookRequest(
        releaseDate = randomLocalDate(),
        quantity = randomNumeric(3).toInt(),
        price = Price(randomPrice(), nextValue<Currency>())
    )

    fun bookEntity() = BookEntity(
        isbn = randomNumeric(17),
        title = randomAlphabetic(15),
        authors = arrayOf(randomAlphabetic(10)),
        description = randomAlphanumeric(25),
        genre = nextValue<GenreEntity>(),
        releaseDate = randomLocalDate(),
        quantity = randomNumeric(3).toInt(),
        price = PriceEntity(randomPrice(), nextValue<CurrencyEntity>())
    )
}

object ReviewTestData {

    fun createReviewRequest(bookId: UUID) = CreateReviewRequest(
        title = randomAlphabetic(15),
        reviewText = randomAlphabetic(25),
        author = randomAlphabetic(10),
        rating = randomRating(),
        bookId = bookId
    )

    fun reviewEntity(bookFk: UUID) = ReviewEntity(
        title = randomAlphabetic(15),
        reviewText = randomAlphabetic(25),
        author = randomAlphabetic(10),
        rating = randomRating(),
        bookFk = bookFk
    )
}