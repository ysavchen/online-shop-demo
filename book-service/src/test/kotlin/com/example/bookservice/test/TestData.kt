package com.example.bookservice.test

import com.example.bookservice.repository.entity.BookEntity
import com.example.bookservice.repository.entity.CurrencyEntity
import com.example.bookservice.repository.entity.GenreEntity
import org.apache.commons.lang3.RandomStringUtils.*
import java.util.*

object TestData {

    fun bookEntity() = BookEntity(
        id = UUID.randomUUID(),
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