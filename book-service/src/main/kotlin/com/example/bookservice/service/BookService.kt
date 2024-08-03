package com.example.bookservice.service

import com.example.bookservice.api.rest.error.BookNotFoundException
import com.example.bookservice.api.rest.error.DuplicateRequestException
import com.example.bookservice.api.rest.model.*
import com.example.bookservice.mapping.BookMapper.toEntity
import com.example.bookservice.mapping.BookMapper.toModel
import com.example.bookservice.mapping.BookMapper.toPagedModel
import com.example.bookservice.mapping.RequestMapper.toPageable
import com.example.bookservice.repository.BookRepository
import com.example.bookservice.repository.BookRepository.Companion.searchSpec
import com.example.bookservice.repository.IdempotencyKeyRepository
import com.example.bookservice.repository.entity.IdempotencyKeyEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.data.web.PagedModel
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class BookService(
    private val bookRepository: BookRepository,
    private val idempotencyKeyRepository: IdempotencyKeyRepository
) {

    @Transactional(readOnly = true)
    fun getBooks(bookRequestParams: BookRequestParams, request: BookSearchRequest?): PagedModel<Book> =
        bookRepository.findAll(searchSpec(request), bookRequestParams.toPageable()).toPagedModel()

    @Transactional(readOnly = true)
    fun getBookById(bookId: UUID): Book = bookRepository.findByIdOrNull(bookId)?.toModel()
        ?: throw BookNotFoundException(bookId)

    @Transactional(readOnly = true)
    fun getBookDescription(bookId: UUID): BookDescription {
        val bookEntity = bookRepository.findByIdOrNull(bookId) ?: throw BookNotFoundException(bookId)
        return BookDescription(bookEntity.description)
    }

    @Transactional
    fun createBook(idempotencyKey: UUID, request: CreateBookRequest): Book {
        val key = idempotencyKeyRepository.findByIdOrNull(idempotencyKey)
        if (key?.bookId != null) throw DuplicateRequestException(key.idempotencyKey, key.bookId)
        val savedBook = bookRepository.save(request.toEntity())
        idempotencyKeyRepository.save(IdempotencyKeyEntity(idempotencyKey, savedBook.id))
        return savedBook.toModel()
    }

}