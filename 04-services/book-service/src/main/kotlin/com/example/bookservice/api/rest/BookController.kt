package com.example.bookservice.api.rest

import com.example.bookservice.api.rest.model.*
import com.example.bookservice.service.BookService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.data.web.PagedModel
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1", produces = [MediaType.APPLICATION_JSON_VALUE])
class BookController(private val bookService: BookService) {

    @Operation(summary = "Search books")
    @PostMapping("/books/search")
    fun books(
        bookRequestParams: BookRequestParams,
        @RequestBody request: BookSearchRequest?
    ): PagedModel<Book> = bookService.getBooks(bookRequestParams, request)

    @Operation(summary = "Get book by id")
    @GetMapping("/books/{bookId}")
    fun bookById(@PathVariable("bookId") bookId: UUID): Book = bookService.getBookById(bookId)

    @Operation(
        summary = "Get description by bookId",
        description = "Book description can be quite large, that's why a separate endpoint is used"
    )
    @GetMapping("/books/{bookId}/description")
    fun bookDescription(@PathVariable("bookId") bookId: UUID): BookDescription =
        bookService.getBookDescription(bookId)

    @Operation(summary = "Create book")
    @PostMapping("/books", consumes = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun createBook(
        @RequestHeader(IDEMPOTENCY_KEY) idempotencyKey: UUID,
        @RequestBody request: CreateBookRequest
    ): Book = bookService.createBook(idempotencyKey, request)

    @Operation(summary = "Update book")
    @PatchMapping("/books/{bookId}", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun updateBook(
        @PathVariable("bookId") bookId: UUID,
        @RequestBody request: UpdateBookRequest
    ): Book = bookService.updateBook(bookId, request)

}