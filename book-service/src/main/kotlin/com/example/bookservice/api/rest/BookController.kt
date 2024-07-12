package com.example.bookservice.api.rest

import com.example.bookservice.api.rest.RestCompanion.BASE_PATH_V1
import com.example.bookservice.api.rest.model.*
import com.example.bookservice.service.BookService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort.Direction
import org.springframework.data.web.PageableDefault
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping(BASE_PATH_V1, produces = [MediaType.APPLICATION_JSON_VALUE])
class BookController(private val bookService: BookService) {

    @PostMapping("/books/search")
    fun books(
        @PageableDefault(size = 10, sort = ["releaseDate"], direction = Direction.ASC)
        pageable: Pageable,
        @RequestBody request: BookSearchRequest?
    ): Page<Book> = bookService.getBooks(pageable, request)

    @GetMapping("/books/{bookId}")
    fun bookById(@PathVariable("bookId") bookId: UUID): Book = bookService.getBookById(bookId)

    @GetMapping("/books/{bookId}/description")
    fun bookDescription(@PathVariable("bookId") bookId: UUID): String? = bookService.getBookDescription(bookId)

    @PostMapping("/books")
    fun createBook(
        @RequestHeader("Idempotency-Key") idempotencyKey: UUID,
        @RequestBody request: CreateBookRequest
    ): Book = bookService.createBook(idempotencyKey, request)

}