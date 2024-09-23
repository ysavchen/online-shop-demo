package com.example.bookservice.mapping

import com.example.bookservice.api.rest.BookRequestParams
import com.example.bookservice.api.rest.ReviewRequestParams
import com.example.bookservice.api.rest.UnsupportedOrderingException
import com.example.bookservice.api.rest.UnsupportedSortingException
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort.Direction

object RequestMapper {

    internal fun BookRequestParams.toPageable(): Pageable =
        PageRequest.of(
            this.page,
            this.pageSize,
            Direction.fromString(parseOrderBy(this.orderBy)),
            parseBookSortBy(this.sortBy)
        )

    internal fun ReviewRequestParams.toPageable(): Pageable =
        PageRequest.of(
            this.page,
            this.pageSize,
            Direction.fromString(parseOrderBy(this.orderBy)),
            parseReviewSortBy(this.sortBy)
        )

    private fun parseBookSortBy(sortBy: String): String =
        when (sortBy) {
            "title" -> "title"
            "release_date" -> "releaseDate"
            else -> throw UnsupportedSortingException(sortBy)
        }

    private fun parseReviewSortBy(sortBy: String): String =
        when (sortBy) {
            "title" -> "title"
            "rating" -> "rating"
            else -> throw UnsupportedSortingException(sortBy)
        }

    private fun parseOrderBy(orderBy: String): String =
        when (orderBy) {
            "asc", "ASC" -> "ASC"
            "desc", "DESC" -> "DESC"
            else -> throw UnsupportedOrderingException(orderBy)
        }
}