package com.example.bookservice.api.rest.model

import com.example.online.shop.model.Description
import com.example.online.shop.model.Isbn
import com.example.online.shop.model.Title
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

data class BookSearchRequest(
    val query: String?,  //search request by book title
    val genre: Genre?,
    val minPrice: BigDecimal?,
    val maxPrice: BigDecimal?
)

data class BooksFilterRequest(
    val bookIds: List<UUID>
)

data class CreateBookRequest(
    val isbn: Isbn,
    val title: Title,
    val authors: List<String>,
    val description: Description?,
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

data class BookDescription(
    val description: Description?
)

enum class Currency {
    RUB, EUR
}

enum class Genre {
    HEALTH, TRAVEL, FICTION
}