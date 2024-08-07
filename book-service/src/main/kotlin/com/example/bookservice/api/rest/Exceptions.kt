package com.example.bookservice.api.rest

import com.example.bookservice.api.rest.model.ErrorCode
import org.springframework.http.HttpStatus

import org.springframework.http.HttpStatusCode
import java.util.*

open class ServiceException(
    message: String,
    val errorCode: ErrorCode,
    val httpStatusCode: HttpStatusCode
) : RuntimeException(message)

class BookNotFoundException(id: UUID) :
    ServiceException("Book not found by id=$id", ErrorCode.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND)

class ReviewNotFoundException(id: UUID) :
    ServiceException("Review not found by id=$id", ErrorCode.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND)

class UnsupportedSortingException(sortBy: String) :
    ServiceException(
        "Sorting by $sortBy is not supported",
        ErrorCode.SORTING_CATEGORY_NOT_SUPPORTED,
        HttpStatus.BAD_REQUEST
    )

class DuplicateRequestException(idempotencyKey: UUID, resourceId: UUID) :
    ServiceException(
        "Duplicate request with idempotencyKey=$idempotencyKey, resource already created with id=$resourceId",
        ErrorCode.REQUEST_ALREADY_PROCESSED,
        HttpStatus.CONFLICT
    )