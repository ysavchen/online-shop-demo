package com.example.bookservice.rest.client

import Book
import java.util.*

class BookServiceClientImpl : BookServiceClient {

    override suspend fun getBookById(bookId: UUID): Book {
        TODO("Not yet implemented")
    }
}