package com.example.bookservice.rest.client

import com.example.bookservice.rest.client.model.Book
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import java.util.*

class BookServiceRestClientImpl(private val webClient: WebClient) : BookServiceRestClient {

    companion object {
        private const val BOOKS_PATH_V1 = "/api/v1/books"
    }

    override suspend fun getBookById(bookId: UUID): Book =
        webClient.get().uri("$BOOKS_PATH_V1/$bookId")
            .retrieve()
            .awaitBody<Book>()

}