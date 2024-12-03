package com.example.deliveryservice.kafka.client.model

import java.util.*

data class DeliveryNotFoundError(
    val message: String,
    val details: ResourceNotFoundDetails,
    val errorCode: ErrorCode,
) : Data {
    constructor(id: UUID) : this(
        message = "Delivery not found by id=$id",
        errorCode = ErrorCode.RESOURCE_NOT_FOUND,
        details = ResourceNotFoundDetails(id)
    )
}

/**
 * Details to process an error by machine
 */
data class ResourceNotFoundDetails(
    val resourceId: UUID
)

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