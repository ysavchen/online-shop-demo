package com.example.deliveryservice.service

import java.util.*

open class ServiceException(message: String, errorCode: ErrorCode) : RuntimeException(message)

class DuplicateMessageException(messageKey: UUID) :
    ServiceException(
        "Duplicate message with key=$messageKey, message already processed",
        ErrorCode.MESSAGE_ALREADY_PROCESSED
    )

enum class ErrorCode {
    MESSAGE_ALREADY_PROCESSED
}