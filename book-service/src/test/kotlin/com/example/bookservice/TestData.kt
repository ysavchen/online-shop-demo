package com.example.bookservice

import com.example.bookservice.repository.entity.BookEntity
import com.example.bookservice.repository.entity.CurrencyEntity
import com.example.bookservice.repository.entity.GenreEntity
import org.apache.commons.lang3.RandomStringUtils.*
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

object TestData {

    fun bookEntity() = BookEntity(
        id = UUID.randomUUID(),
        title = randomAlphabetic(15),
        authors = arrayOf(randomAlphabetic(10)),
        description = randomAlphanumeric(25),
        genre = GenreEntity.TRAVEL,
        releaseDate = LocalDate.now(),
        quantity = randomNumeric(3).toInt(),
        price = BigDecimal(Math.random()),
        currency = CurrencyEntity.RUB
    )
}