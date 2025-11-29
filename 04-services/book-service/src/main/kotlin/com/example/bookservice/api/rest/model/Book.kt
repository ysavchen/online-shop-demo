package com.example.bookservice.api.rest.model

import com.example.online.shop.model.*
import java.time.LocalDate
import java.util.*

data class BookSearchRequest(
    val query: SearchQuery?,  //search request by book title
    val genre: Genre?,
    val minPrice: PriceValue?,
    val maxPrice: PriceValue?
)

data class BooksFilterRequest(
    val bookIds: List<UUID>
)

data class CreateBookRequest(
    val isbn: Isbn,
    val title: Title,
    val authors: List<Author>,
    val description: Description?,
    val genre: Genre,
    val releaseDate: LocalDate?,
    val quantity: Quantity,
    val price: Price?
)

data class UpdateBookRequest(
    val releaseDate: LocalDate?,
    val quantity: Quantity,
    val price: Price?
)

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

data class BookDescription(
    val description: Description?
)

enum class Currency {
    RUB, EUR
}

enum class Genre {
    HEALTH, TRAVEL, FICTION
}