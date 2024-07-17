package com.example.orderservice.api.rest.error

import com.example.orderservice.api.rest.model.ErrorCode
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import java.lang.RuntimeException
import java.util.*

open class ServiceException(
    message: String,
    val errorCode: ErrorCode,
    val httpStatusCode: HttpStatusCode
) : RuntimeException(message)

class OrderNotFoundException(id: UUID) :
    ServiceException("Order not found by id=$id", ErrorCode.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND)

class UnsupportedSortingException(sortBy: String) :
    ServiceException(
        "Sorting by $sortBy is not supported",
        ErrorCode.SORTING_CATEGORY_NOT_SUPPORTED,
        HttpStatus.BAD_REQUEST
    )