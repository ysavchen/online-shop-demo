package com.example.bookservice.api.rest.model

import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

data class BookSearchRequest(
    val query: String?,   // поисковый запрос по названию книги
    val genre: String?,   // фильтрация по жанру
    val minPrice: BigDecimal?,  // фильтрация по диапазону цены
    val maxPrice: BigDecimal?
)

data class CreateBookRequest(
    val title: String,
    val authors: List<String>,
    val description: String?,
    val genre: Genre,
    val releaseDate: LocalDate?,
    val quantity: Int,
    val price: BigDecimal?,
    val currency: Currency?
)

data class Book(
    val id: UUID,
    val title: String,
    val authors: List<String>,
    val genre: Genre,
    val releaseDate: LocalDate?,
    val price: BigDecimal?,
    val currency: Currency?
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