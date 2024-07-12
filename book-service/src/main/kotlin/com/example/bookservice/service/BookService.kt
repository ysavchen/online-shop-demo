package com.example.bookservice.service

import com.example.bookservice.api.rest.model.*
import com.example.bookservice.mapping.BookMapper.toModel
import com.example.bookservice.repository.BookRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class BookService(private val bookRepository: BookRepository) {

    @Transactional(readOnly = true)
    fun getBooks(paging: Paging, sorting: Sorting, request: BookSearchRequest?): Page<Book> {
        val pageRequest = PageRequest.of(paging.page, paging.pageSize)
        return bookRepository.findAll(pageRequest).map { it.toModel() }
    }

    @Transactional(readOnly = true)
    fun getBookById(bookId: UUID): Book = bookRepository.findByIdOrNull(bookId)?.toModel()
        ?: throw BookNotFoundException(bookId)

    @Transactional(readOnly = true)
    fun getBookDescription(bookId: UUID): String? = bookRepository.findByIdOrNull(bookId)?.description
        ?: throw BookNotFoundException(bookId)

    @Transactional
    fun createBook(idempotencyKey: UUID, request: CreateBookRequest): Book = TODO()

}