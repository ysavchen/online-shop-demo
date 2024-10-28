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
    REQUEST_VALIDATION_ERROR,
    REQUEST_ALREADY_PROCESSED,
    INVALID_ORDER_STATUS_UPDATE,
    ORDER_ITEM_VALIDATION_ERROR,
    INTERNAL_SERVER_ERROR
}