package com.example.bookservice.mapping

import com.example.bookservice.api.rest.model.BookRequestParams
import com.example.bookservice.api.rest.model.ReviewRequestParams
import com.example.bookservice.service.UnsupportedSortingException
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort.Direction

object RequestMapper {

    internal fun BookRequestParams.toPageable(): Pageable =
        PageRequest.of(
            this.page,
            this.pageSize,
            Direction.fromString(this.orderBy),
            parseBookSortBy(this.sortBy)
        )

    internal fun ReviewRequestParams.toPageable(): Pageable =
        PageRequest.of(
            this.page,
            this.pageSize,
            Direction.fromString(this.orderBy),
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
}