package com.example.bookservice.api.rest.model

import java.time.OffsetDateTime
import java.util.*

data class BookRequestParams(
    val page: Int = 1,
    val pageSize: Int = 10,
    val sortBy: String = "title",
    val orderBy: String = "asc"
)

data class ReviewRequestParams(
    val page: Int = 1,
    val pageSize: Int = 10,
    val sortBy: String = "rating",
    val orderBy: String = "desc"
)

data class ErrorResponse(
    val errorId: UUID,
    val timestamp: OffsetDateTime,
    val path: String,
    val code: ErrorCode,
    val message: String
)

enum class ErrorCode {
    RESOURCE_NOT_FOUND,
    SORTING_CATEGORY_NOT_SUPPORTED,
    REQUEST_ALREADY_PROCESSED,
    INTERNAL_SERVER_ERROR
}