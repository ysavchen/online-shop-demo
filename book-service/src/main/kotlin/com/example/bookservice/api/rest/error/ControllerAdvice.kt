package com.example.bookservice.api.rest.error

import com.example.bookservice.api.rest.model.ErrorCode
import com.example.bookservice.api.rest.model.ErrorResponse
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.OffsetDateTime
import java.util.*

@RestControllerAdvice
class ControllerAdvice {

    companion object {
        private val logger = KotlinLogging.logger(this::class.java.name)
    }

    @ExceptionHandler
    fun serviceException(ex: ServiceException, request: HttpServletRequest): ResponseEntity<ErrorResponse> =
        ResponseEntity(
            ErrorResponse(
                errorId = UUID.randomUUID(),
                timestamp = OffsetDateTime.now(),
                path = request.requestURI,
                code = ex.errorCode,
                message = ex.message!!
            ),
            ex.httpStatusCode
        )

    @ExceptionHandler
    fun exception(ex: Exception, request: HttpServletRequest): ResponseEntity<ErrorResponse> {
        val response = ResponseEntity(
            ErrorResponse(
                errorId = UUID.randomUUID(),
                timestamp = OffsetDateTime.now(),
                path = request.requestURI,
                code = ErrorCode.INTERNAL_SERVER_ERROR,
                // message must be logged, but not included in response due to security reasons
                message = "Internal Server Error"
            ),
            HttpStatus.INTERNAL_SERVER_ERROR
        )
        logger.error { "Error: ${ex.message}, response: $response" }
        return response
    }
}