package com.example.bookservice.mapping

import com.example.bookservice.api.rest.model.*
import com.example.bookservice.repository.entity.BookEntity
import com.example.bookservice.repository.entity.CurrencyEntity
import com.example.bookservice.repository.entity.GenreEntity
import com.example.bookservice.repository.entity.PriceEntity
import com.example.online.shop.model.Author
import com.example.online.shop.model.PriceValue
import org.springframework.data.domain.Page
import org.springframework.data.web.PagedModel

object BookMapper {

    internal fun BookEntity.toModel() = Book(
        id = id!!,
        isbn = isbn,
        title = title,
        authors = authors.map { Author(it) }.toList(),
        genre = genre.toModel(),
        releaseDate = releaseDate,
        quantity = quantity,
        price = price.toModel()
    )

    internal fun CreateBookRequest.toEntity() = BookEntity(
        isbn = isbn,
        title = title,
        authors = authors.map { it.formattedValue }.toTypedArray(),
        description = description,
        genre = genre.toEntity(),
        releaseDate = releaseDate,
        quantity = quantity,
        price = price?.toEntity() ?: PriceEntity(value = null, currency = null)
    )

    internal fun Page<BookEntity>.toPagedModel() = PagedModel(
        this.map { it.toModel() }
    )

    internal fun GenreEntity.toModel() = when (this) {
        GenreEntity.HEALTH -> Genre.HEALTH
        GenreEntity.TRAVEL -> Genre.TRAVEL
        GenreEntity.FICTION -> Genre.FICTION
    }

    internal fun Genre.toEntity() = when (this) {
        Genre.HEALTH -> GenreEntity.HEALTH
        Genre.TRAVEL -> GenreEntity.TRAVEL
        Genre.FICTION -> GenreEntity.FICTION
    }

    internal fun CurrencyEntity.toModel() = when (this) {
        CurrencyEntity.RUB -> Currency.RUB
        CurrencyEntity.EUR -> Currency.EUR
    }

    internal fun Currency.toEntity() = when (this) {
        Currency.RUB -> CurrencyEntity.RUB
        Currency.EUR -> CurrencyEntity.EUR
    }

    internal fun PriceEntity.toModel() =
        if (value == null && currency == null) null
        else if (value == null || currency == null)
            throw IllegalStateException("Price is inconsistent: value=$value, currency=$currency")
        else Price(value = PriceValue(value!!), currency = currency?.toModel()!!)

    internal fun Price.toEntity() = PriceEntity(value = value.formattedValue, currency = currency.toEntity())

}