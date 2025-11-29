package com.example.bookservice.rest.client.model

import com.example.online.shop.model.*
import java.time.LocalDate
import java.util.*

data class Book(
    val id: UUID,
    val isbn: Isbn,
    val title: Title,
    val authors: List<Author>,
    val genre: Genre,
    val releaseDate: LocalDate?,
    val quantity: Quantity,
    val price: Price?
)

data class Price(
    val value: PriceValue,
    val currency: Currency
)

enum class Currency {
    RUB, EUR
}

enum class Genre {
    HEALTH, TRAVEL, FICTION
}