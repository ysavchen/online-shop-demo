package com.example.bookservice.feign.client.model

import com.example.online.shop.model.Isbn
import com.example.online.shop.model.Title
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

data class Book(
    val id: UUID,
    val isbn: Isbn,
    val title: Title,
    val authors: List<String>,
    val genre: Genre,
    val releaseDate: LocalDate?,
    val quantity: Int,
    val price: Price?
)

data class Price(
    val value: BigDecimal,
    val currency: Currency
)

enum class Currency {
    RUB, EUR
}

enum class Genre {
    HEALTH, TRAVEL, FICTION
}