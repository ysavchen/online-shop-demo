package com.example.orderservice.api.rest

import com.example.orderservice.api.rest.model.ErrorCode
import com.example.orderservice.api.rest.model.Status
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import java.util.*

open class ServiceException(
    message: String,
    val errorCode: ErrorCode,
    val httpStatusCode: HttpStatusCode
) : RuntimeException(message)

class OrderNotFoundException(id: UUID) :
    ServiceException("Order not found by id=$id", ErrorCode.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND)

class RequestValidationException(message: String) :
    ServiceException(message, ErrorCode.REQUEST_VALIDATION_ERROR, HttpStatus.BAD_REQUEST)

class DuplicateRequestException(idempotencyKey: UUID, resourceId: UUID) :
    ServiceException(
        "Duplicate request with idempotencyKey=$idempotencyKey, resource already created with id=$resourceId",
        ErrorCode.REQUEST_ALREADY_PROCESSED,
        HttpStatus.CONFLICT
    )

class InvalidOrderStatusUpdate(orderId: UUID, currentStatus: Status, newStatus: Status) :
    ServiceException(
        "Update order with id=$orderId from status $currentStatus to $newStatus is not valid",
        ErrorCode.INVALID_ORDER_STATUS_UPDATE,
        HttpStatus.FORBIDDEN
    )

class OrderItemValidationException(message: String) :
    ServiceException(
        message,
        ErrorCode.ORDER_ITEM_VALIDATION_ERROR,
        HttpStatus.FORBIDDEN
    )