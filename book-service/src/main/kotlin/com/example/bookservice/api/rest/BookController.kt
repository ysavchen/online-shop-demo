package com.example.bookservice.api.rest

import com.example.bookservice.api.rest.RestCompanion.BASE_PATH_V1
import com.example.bookservice.service.BookService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping(BASE_PATH_V1, produces = [MediaType.APPLICATION_JSON_VALUE])
class BookController(private val bookService: BookService) {

    @GetMapping("/books")
    fun books(): List<Book> = bookService.getBooks()

    @GetMapping("/books/{bookId}")
    fun bookById(@PathVariable("bookId") bookId: UUID): Book = bookService.getBookById(bookId)


}