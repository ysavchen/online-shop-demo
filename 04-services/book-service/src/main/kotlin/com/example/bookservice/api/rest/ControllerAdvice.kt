package com.example.bookservice.api.rest

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
        private val logger = KotlinLogging.logger(ControllerAdvice::class.java.name)
    }

    @ExceptionHandler
    fun serviceException(ex: ServiceException, request: HttpServletRequest): ResponseEntity<ErrorResponse> =
        ResponseEntity(
            ErrorResponse(
                errorId = UUID.randomUUID(),
                timestamp = OffsetDateTime.now(),
                path = request.requestURI,
                code = ex.errorCode,
                message = ex.message
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
                message = null
            ),
            HttpStatus.INTERNAL_SERVER_ERROR
        )
        logger.error(ex) { "Error response: $response" }
        return response
    }
}