package com.example.bookservice.rest.client.error

import com.example.bookservice.rest.client.model.Book
import io.netty.handler.timeout.TimeoutException
import org.springframework.web.reactive.function.client.WebClientRequestException
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.reactive.function.client.WebClientResponseException.NotFound
import reactor.core.publisher.Mono
import java.net.ConnectException
import java.util.*

internal object ErrorHandler {

    fun fallback(bookId: UUID, throwable: Throwable): Mono<Book> =
        when (throwable) {
            is WebClientRequestException -> requestException(bookId, throwable)
            is WebClientResponseException -> responseException(bookId, throwable)
            else -> Mono.error(throwable)
        }

    private fun requestException(bookId: UUID, ex: WebClientRequestException): Mono<Book> =
        when (ex.cause) {
            is ConnectException -> Mono.error(DownstreamServiceUnavailableException("Request failed for bookId=$bookId: book-service is not available (connection timeout)"))
            is TimeoutException -> Mono.error(DownstreamServiceUnavailableException("Request failed for bookId=$bookId: book-service is not available (response timeout)"))
            else -> Mono.error(ex)
        }

    private fun responseException(bookId: UUID, ex: WebClientResponseException): Mono<Book> =
        when (ex) {
            is NotFound -> Mono.error(BookNotFoundException(bookId))
            else -> Mono.error(ex)
        }
}