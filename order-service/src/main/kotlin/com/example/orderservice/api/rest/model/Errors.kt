package com.example.orderservice.api.rest.model

import java.time.OffsetDateTime
import java.util.*

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