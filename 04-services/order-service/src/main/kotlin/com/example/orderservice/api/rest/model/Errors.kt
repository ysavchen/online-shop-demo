package com.example.orderservice.api.rest.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonFormat.Shape
import java.time.OffsetDateTime
import java.util.*

data class ErrorResponse(
    val errorId: UUID,
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    val timestamp: OffsetDateTime,
    val path: String,
    val code: ErrorCode,
    val message: String
)

enum class ErrorCode {
    RESOURCE_NOT_FOUND,
    SORTING_CATEGORY_NOT_SUPPORTED,
    REQUEST_ALREADY_PROCESSED,
    INVALID_ORDER_STATUS_UPDATE,
    INTERNAL_SERVER_ERROR
}