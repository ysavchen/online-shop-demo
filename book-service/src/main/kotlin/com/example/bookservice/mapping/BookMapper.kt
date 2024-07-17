package com.example.bookservice.mapping

import com.example.bookservice.api.rest.model.Book
import com.example.bookservice.api.rest.model.CreateBookRequest
import com.example.bookservice.api.rest.model.Currency
import com.example.bookservice.api.rest.model.Genre
import com.example.bookservice.repository.entity.BookEntity
import com.example.bookservice.repository.entity.CurrencyEntity
import com.example.bookservice.repository.entity.GenreEntity
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

    internal fun CreateBookRequest.toEntity() = BookEntity(
        title = title,
        authors = authors.toTypedArray(),
        description = description,
        genre = genre.toEntity(),
        releaseDate = releaseDate,
        price = price,
        currency = currency?.toEntity()
    )

    internal fun Page<BookEntity>.toPagedModel() = PagedModel(
        this.map { it.toModel() }
    )

    private fun GenreEntity.toModel() = when (this) {
        GenreEntity.HEALTH -> Genre.HEALTH
        GenreEntity.TRAVEL -> Genre.TRAVEL
        GenreEntity.FICTION -> Genre.FICTION
    }

    private fun Genre.toEntity() = when (this) {
        Genre.HEALTH -> GenreEntity.HEALTH
        Genre.TRAVEL -> GenreEntity.TRAVEL
        Genre.FICTION -> GenreEntity.FICTION
    }

    private fun CurrencyEntity.toModel() = when (this) {
        CurrencyEntity.RUB -> Currency.RUB
        CurrencyEntity.EUR -> Currency.EUR
    }

    private fun Currency.toEntity() = when (this) {
        Currency.RUB -> CurrencyEntity.RUB
        Currency.EUR -> CurrencyEntity.EUR
    }

}