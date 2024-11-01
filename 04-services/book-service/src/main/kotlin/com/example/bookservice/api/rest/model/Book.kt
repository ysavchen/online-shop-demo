package com.example.bookservice.api.rest.model

import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

data class BookSearchRequest(
    val query: String?,  //search request by book title
    val genre: String?,
    val minPrice: BigDecimal?,
    val maxPrice: BigDecimal?
)

data class BooksFilterRequest(
    val bookIds: List<UUID>
)

data class CreateBookRequest(
    val isbn: String,
    val title: String,
    val authors: List<String>,
    val description: String?,
    val genre: Genre,
    val releaseDate: LocalDate?,
    val quantity: Int,
    val price: Price?
)

data class UpdateBookRequest(
    val releaseDate: LocalDate?,
    val quantity: Int,
    val price: Price?
)

data class Book(
    val id: UUID,
    val isbn: String,
    val title: String,
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

data class BookDescription(
    val description: String?
)

enum class Currency {
    RUB, EUR
}

enum class Genre {
    HEALTH, TRAVEL, FICTION
}