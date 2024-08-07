package com.example.bookservice.test

import com.example.bookservice.api.rest.model.CreateBookRequest
import com.example.bookservice.api.rest.model.Currency
import com.example.bookservice.api.rest.model.Genre
import com.example.bookservice.repository.entity.BookEntity
import com.example.bookservice.repository.entity.CurrencyEntity
import com.example.bookservice.repository.entity.GenreEntity
import com.example.bookservice.repository.entity.ReviewEntity
import org.apache.commons.lang3.RandomStringUtils.*
import java.util.*

object BookTestData {

    fun createBookRequest() = CreateBookRequest(
        title = randomAlphabetic(15),
        authors = listOf(randomAlphabetic(10)),
        description = randomAlphanumeric(25),
        genre = nextValue<Genre>(),
        releaseDate = randomLocalDate(),
        quantity = randomNumeric(3).toInt(),
        price = randomPrice(),
        currency = nextValue<Currency>()
    )

    fun bookEntity() = BookEntity(
        title = randomAlphabetic(15),
        authors = arrayOf(randomAlphabetic(10)),
        description = randomAlphanumeric(25),
        genre = nextValue<GenreEntity>(),
        releaseDate = randomLocalDate(),
        quantity = randomNumeric(3).toInt(),
        price = randomPrice(),
        currency = nextValue<CurrencyEntity>()
    )
}

object ReviewTestData {

    fun reviewEntity(bookFk: UUID) = ReviewEntity(
        title = randomAlphabetic(15),
        reviewText = randomAlphabetic(25),
        author = randomAlphabetic(10),
        rating = randomRating(),
        bookFk = bookFk
    )
}