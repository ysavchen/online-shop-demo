package com.example.service.support.error

import java.time.OffsetDateTime
import java.util.*

data class ErrorResponse(
    val errorId: UUID = UUID.randomUUID(),
    val timestamp: OffsetDateTime = OffsetDateTime.now(),
    val path: String,
    val code: ErrorCode,
    val message: String?
)

interface ErrorCode

enum class CommonErrorCode : ErrorCode {
    RESOURCE_NOT_FOUND,
    REQUEST_VALIDATION_ERROR,
    REQUEST_ALREADY_PROCESSED,
    DOWNSTREAM_SERVICE_UNAVAILABLE_ERROR,
    INTERNAL_SERVER_ERROR
}