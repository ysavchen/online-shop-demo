package com.example.bookservice.rest.client.error

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.web.server.ResponseStatusException
import java.util.*

open class BookServiceClientException(
    message: String,
    val errorCode: ErrorCode,
    val httpStatusCode: HttpStatusCode
) : ResponseStatusException(httpStatusCode, message)

class BookNotFoundException(id: UUID) :
    BookServiceClientException("Book not found by id=$id", ErrorCode.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND)

class DownstreamServiceUnavailableException(message: String) :
    BookServiceClientException(message, ErrorCode.DOWNSTREAM_SERVICE_UNAVAILABLE_ERROR, HttpStatus.GATEWAY_TIMEOUT)

enum class ErrorCode {
    RESOURCE_NOT_FOUND,
    DOWNSTREAM_SERVICE_UNAVAILABLE_ERROR
}