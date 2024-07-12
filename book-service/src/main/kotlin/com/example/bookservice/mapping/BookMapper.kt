package com.example.bookservice.mapping

import com.example.bookservice.api.rest.model.Book
import com.example.bookservice.api.rest.model.Currency
import com.example.bookservice.repository.BookEntity
import com.example.bookservice.repository.CurrencyEntity
import org.springframework.data.domain.Page
import org.springframework.data.web.PagedModel

object BookMapper {

    internal fun BookEntity.toModel() = Book(
        id = id!!,
        title = title,
        authors = authors.toList(),
        price = price,
        currency = currency?.toModel()
    )

    internal fun CurrencyEntity.toModel() = when (this) {
        CurrencyEntity.RUB -> Currency.RUB
    }

    internal fun Page<BookEntity>.toPagedModel() = PagedModel(
        this.map { it.toModel() }
    )
}