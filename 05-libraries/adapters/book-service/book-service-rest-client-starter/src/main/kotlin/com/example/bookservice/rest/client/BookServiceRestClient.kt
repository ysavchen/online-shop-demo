package com.example.bookservice.rest.client

import com.example.bookservice.rest.client.model.Book
import java.util.UUID

interface BookServiceRestClient {

    suspend fun getBookById(bookId: UUID): Book
}