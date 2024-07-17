package com.example.orderservice.api.rest.model

import java.time.OffsetDateTime
import java.util.*

data class OrderRequestParams(
    val page: Int = 1,
    val pageSize: Int = 10,
    val sortBy: String = "created_at",
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
    INTERNAL_SERVER_ERROR
}