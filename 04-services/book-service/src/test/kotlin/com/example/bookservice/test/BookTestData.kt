package com.example.bookservice.test

import com.example.bookservice.api.rest.model.CreateBookRequest
import com.example.bookservice.api.rest.model.Currency
import com.example.bookservice.api.rest.model.Genre
import com.example.bookservice.api.rest.model.Price
import com.example.bookservice.api.rest.model.UpdateBookRequest
import com.example.bookservice.repository.entity.BookEntity
import com.example.bookservice.repository.entity.CurrencyEntity
import com.example.bookservice.repository.entity.GenreEntity
import com.example.bookservice.repository.entity.PriceEntity
import com.example.online.shop.model.Description
import com.example.online.shop.model.Isbn
import com.example.online.shop.model.Title
import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric
import org.apache.commons.lang3.RandomStringUtils.randomNumeric

object BookTestData {

    fun createBookRequest() = CreateBookRequest(
        isbn = Isbn.valueOf("9781525826689"),
        title = Title.valueOf(randomAlphabetic(15)),
        authors = listOf(randomAlphabetic(10)),
        description = Description.valueOf(randomAlphanumeric(25)),
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