package com.example.bookservice.mapping

import com.example.bookservice.service.UnsupportedSortingException
import com.example.bookservice.api.rest.model.BookRequestParams
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort.Direction

object RequestMapper {

    internal fun BookRequestParams.toPageable(): Pageable =
        PageRequest.of(
            this.page,
            this.pageSize,
            Direction.fromString(this.orderBy),
            parseSortBy(this.sortBy)
        )

    private fun parseSortBy(sortBy: String): String =
        when (sortBy) {
            "title" -> "title"
            "release_date" -> "releaseDate"
            else -> throw UnsupportedSortingException(sortBy)
        }
}