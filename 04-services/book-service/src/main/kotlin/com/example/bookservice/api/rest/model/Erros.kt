package com.example.bookservice.api.rest.model

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
    SORTING_PARAMETER_NOT_SUPPORTED,
    ORDERING_PARAMETER_NOT_SUPPORTED,
    REQUEST_ALREADY_PROCESSED,
    INTERNAL_SERVER_ERROR
}