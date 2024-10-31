package com.example.bookservice.rest.client

import com.example.bookservice.rest.client.model.Book
import java.util.*

interface BookServiceClient {

    suspend fun getBookById(bookId: UUID): Book
}