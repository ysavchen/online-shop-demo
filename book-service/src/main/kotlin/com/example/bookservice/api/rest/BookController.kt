package com.example.bookservice.api.rest

import com.example.bookservice.api.rest.model.Book
import com.example.bookservice.api.rest.model.BookDescription
import com.example.bookservice.api.rest.model.BookSearchRequest
import com.example.bookservice.api.rest.model.CreateBookRequest
import com.example.bookservice.service.BookService
import org.springframework.data.web.PagedModel
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1", produces = [MediaType.APPLICATION_JSON_VALUE])
class BookController(private val bookService: BookService) {

    @PostMapping("/books/search")
    fun books(
        bookRequestParams: BookRequestParams,
        @RequestBody request: BookSearchRequest?
    ): PagedModel<Book> = bookService.getBooks(bookRequestParams, request)

    @GetMapping("/books/{bookId}")
    fun bookById(@PathVariable("bookId") bookId: UUID): Book = bookService.getBookById(bookId)

    @GetMapping("/books/{bookId}/description")
    fun bookDescription(@PathVariable("bookId") bookId: UUID): BookDescription =
        bookService.getBookDescription(bookId)

    @PostMapping("/books", consumes = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun createBook(
        @RequestHeader(IDEMPOTENCY_KEY) idempotencyKey: UUID,
        @RequestBody request: CreateBookRequest
    ): Book = bookService.createBook(idempotencyKey, request)

}