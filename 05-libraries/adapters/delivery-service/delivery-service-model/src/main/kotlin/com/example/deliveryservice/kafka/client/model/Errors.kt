package com.example.deliveryservice.kafka.client.model

import java.util.*

open class ServiceError(val message: String, val errorCode: ErrorCode) : Data

class DuplicateMessageError(messageKey: UUID, resourceId: UUID, resource: String) :
    ServiceError(
        "Duplicate message with key=$messageKey, $resource already created with id=$resourceId",
        ErrorCode.MESSAGE_ALREADY_PROCESSED
    )

enum class ErrorCode {
    MESSAGE_ALREADY_PROCESSED
}