package com.example.deliveryservice.kafka.client.model

import java.util.*

data class DeliveryNotFoundError(
    val message: String,
    val errorCode: ErrorCode,
) : Data {
    constructor(message: String) : this(
        message = message,
        errorCode = ErrorCode.RESOURCE_NOT_FOUND
    )
}

data class DuplicateMessageError(
    val message: String,
    val details: DuplicateMessageDetails,
    val errorCode: ErrorCode
) : Data {
    constructor(messageKey: UUID, resourceId: UUID, resource: String) : this(
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