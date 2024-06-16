package com.example.bookservice.mapping

import com.example.bookservice.api.rest.Book
import com.example.bookservice.repository.BookEntity

object BookMapper {

    internal fun BookEntity.toModel() = Book(
        id = id,
        title = title,
        authors = authors,
        description = description,
        price = price
    )
}