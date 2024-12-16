package com.example.bookservice.rest.client

import com.example.bookservice.rest.client.error.ErrorHandler.fallback
import com.example.bookservice.rest.client.model.Book
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker
import org.springframework.web.reactive.function.client.WebClient
import java.util.*

class BookServiceRestClientImpl(
    private val webClient: WebClient,
    private val circuitBreaker: ReactiveCircuitBreaker
) : BookServiceRestClient {

    companion object {
        private const val BOOKS_PATH_V1 = "/api/v1/books"
    }

    override suspend fun getBookById(bookId: UUID): Book =
        circuitBreaker.run(
            webClient.get().uri("$BOOKS_PATH_V1/$bookId")
                .retrieve()
                .bodyToMono(Book::class.java)
        ) { ex -> fallback(bookId, ex) }
            .awaitSingle()

}