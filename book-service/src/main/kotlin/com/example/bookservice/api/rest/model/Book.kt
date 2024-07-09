package com.example.bookservice.api.rest.model

import java.math.BigDecimal
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
    val price: BigDecimal?,
    val currency: Currency?
)

data class Book(
    val id: UUID,
    val title: String,
    val authors: List<String>,
    val description: String?,
    val price: BigDecimal?,
    val currency: Currency?
)

enum class Currency {
    RUB
}