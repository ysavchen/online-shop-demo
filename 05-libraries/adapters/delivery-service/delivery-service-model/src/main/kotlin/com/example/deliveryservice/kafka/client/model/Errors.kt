package com.example.deliveryservice.kafka.client.model

import java.time.OffsetDateTime
import java.util.*

data class DeliveryNotFoundError(
    val errorId: UUID,
    val timestamp: OffsetDateTime,
    val message: String,
    val errorCode: ErrorCode,
) : Data {
    constructor(message: String) : this(
        errorId = UUID.randomUUID(),
        timestamp = OffsetDateTime.now(),
        message = message,
        errorCode = ErrorCode.RESOURCE_NOT_FOUND
    )
}

data class DuplicateMessageError(
    val errorId: UUID,
    val timestamp: OffsetDateTime,
    val message: String,
    val details: DuplicateMessageDetails,
    val errorCode: ErrorCode
) : Data {
    constructor(messageKey: UUID, resourceId: UUID, resource: String) : this(
        errorId = UUID.randomUUID(),
        timestamp = OffsetDateTime.now(),
        message = "Duplicate message with key=$messageKey, $resource already created with id=$resourceId",
        errorCode = ErrorCode.DUPLICATE_MESSAGE_ERROR,
        details = DuplicateMessageDetails(messageKey, resourceId, resource)
    )
}

/**
 * Details to process an error by machine
 */
data class DuplicateMessageDetails(
    val messageKey: UUID,
    val resourceId: UUID,
    val resource: String
)

enum class ErrorCode {
    RESOURCE_NOT_FOUND,
    DUPLICATE_MESSAGE_ERROR
}