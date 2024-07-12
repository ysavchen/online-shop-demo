package com.example.bookservice.service

import com.example.bookservice.BookNotFoundException
import com.example.bookservice.api.rest.model.Book
import com.example.bookservice.api.rest.model.BookSearchRequest
import com.example.bookservice.api.rest.model.CreateBookRequest
import com.example.bookservice.api.rest.model.PageRequestParams
import com.example.bookservice.mapping.BookMapper.toModel
import com.example.bookservice.mapping.BookMapper.toPagedModel
import com.example.bookservice.mapping.RequestMapper.toPageable
import com.example.bookservice.repository.BookRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.data.web.PagedModel
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class BookService(private val bookRepository: BookRepository) {

    @Transactional(readOnly = true)
    fun getBooks(pageRequestParams: PageRequestParams, request: BookSearchRequest?): PagedModel<Book> =
        bookRepository.findAll(pageRequestParams.toPageable()).toPagedModel()

    @Transactional(readOnly = true)
    fun getBookById(bookId: UUID): Book = bookRepository.findByIdOrNull(bookId)?.toModel()
        ?: throw BookNotFoundException(bookId)

    @Transactional(readOnly = true)
    fun getBookDescription(bookId: UUID): String? = bookRepository.findByIdOrNull(bookId)?.description
        ?: throw BookNotFoundException(bookId)

    @Transactional
    fun createBook(idempotencyKey: UUID, request: CreateBookRequest): Book = TODO()

}