package com.example.bookservice.mapping

import com.example.bookservice.api.rest.model.Book
import com.example.bookservice.api.rest.model.Currency
import com.example.bookservice.api.rest.model.Genre
import com.example.bookservice.repository.BookEntity
import com.example.bookservice.repository.CurrencyEntity
import com.example.bookservice.repository.GenreEntity
import org.springframework.data.domain.Page
import org.springframework.data.web.PagedModel

object BookMapper {

    internal fun BookEntity.toModel() = Book(
        id = id!!,
        title = title,
        authors = authors.toList(),
        genre = genre.toModel(),
        releaseDate = releaseDate,
        price = price,
        currency = currency?.toModel()
    )

    internal fun GenreEntity.toModel() = when (this) {
        GenreEntity.HEALTH -> Genre.HEALTH
        GenreEntity.TRAVEL -> Genre.TRAVEL
        GenreEntity.FICTION -> Genre.FICTION
    }

    internal fun CurrencyEntity.toModel() = when (this) {
        CurrencyEntity.RUB -> Currency.RUB
    }

    internal fun Page<BookEntity>.toPagedModel() = PagedModel(
        this.map { it.toModel() }
    )
}