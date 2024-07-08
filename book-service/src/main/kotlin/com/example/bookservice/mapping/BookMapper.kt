package com.example.bookservice.mapping

import com.example.bookservice.api.rest.Book
import com.example.bookservice.api.rest.Currency
import com.example.bookservice.repository.BookEntity
import com.example.bookservice.repository.CurrencyEntity

object BookMapper {

    internal fun BookEntity.toModel() = Book(
        id = id!!,
        title = title,
        authors = authors.toList(),
        description = description,
        price = price,
        currency = currency?.toModel()
    )

    internal fun CurrencyEntity.toModel() = when (this) {
        CurrencyEntity.RUB -> Currency.RUB
    }
}