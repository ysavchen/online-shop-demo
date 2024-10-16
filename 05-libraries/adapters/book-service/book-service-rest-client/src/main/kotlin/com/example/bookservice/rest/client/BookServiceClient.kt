package com.example.bookservice.rest.client

import Book
import java.util.*

interface BookServiceClient {

    suspend fun getBookById(bookId: UUID): Book
}