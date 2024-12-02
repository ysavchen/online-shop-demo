package com.example.deliveryservice.kafka.client.model

import java.util.*

open class ServiceError(
    val message: String,
    val errorCode: ErrorCode,
    val details: Details? = null
) : Data

/**
 * Details to process an error by machine
 */
interface Details

class DuplicateMessageError(messageKey: UUID, resourceId: UUID, resource: String) :
    ServiceError(
        "Duplicate message with key=$messageKey, $resource already created with id=$resourceId",
        ErrorCode.MESSAGE_ALREADY_PROCESSED,
        DuplicateMessageDetails(messageKey, resourceId, resource)
    )

data class DuplicateMessageDetails(
    val messageKey: UUID,
    val resourceId: UUID,
    val resource: String
) : Details

class DeliveryNotFoundError(id: UUID) :
    ServiceError(
        "Delivery not found by id=$id",
        ErrorCode.RESOURCE_NOT_FOUND,
        ResourceNotFoundDetails(id)
    )

data class ResourceNotFoundDetails(
    val resourceId: UUID
) : Details

enum class ErrorCode {
    RESOURCE_NOT_FOUND,
    MESSAGE_ALREADY_PROCESSED
}