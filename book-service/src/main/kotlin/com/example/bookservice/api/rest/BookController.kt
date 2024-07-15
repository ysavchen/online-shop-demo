package com.example.bookservice.api.rest

import com.example.bookservice.api.rest.RestCompanion.BASE_PATH_V1
import com.example.bookservice.api.rest.model.*
import com.example.bookservice.service.BookService
import org.springframework.data.web.PagedModel
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping(BASE_PATH_V1, produces = [MediaType.APPLICATION_JSON_VALUE])
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
    fun createBook(
        @RequestHeader("Idempotency-Key") idempotencyKey: UUID,
        @RequestBody request: CreateBookRequest
    ): Book = bookService.createBook(idempotencyKey, request)

}