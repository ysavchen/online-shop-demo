package com.example.bookservice.service

import com.example.bookservice.api.rest.BookNotFoundException
import com.example.bookservice.api.rest.DuplicateRequestException
import com.example.bookservice.api.rest.RequestValidationException
import com.example.bookservice.api.rest.model.*
import com.example.bookservice.mapping.BookMapper.toEntity
import com.example.bookservice.mapping.BookMapper.toModel
import com.example.bookservice.mapping.BookMapper.toPagedModel
import com.example.bookservice.mapping.RequestMapper.toPageable
import com.example.bookservice.repository.BookRepository
import com.example.bookservice.repository.BookRepository.Companion.searchSpec
import com.example.bookservice.repository.IdempotencyKeyRepository
import com.example.bookservice.repository.entity.IdempotencyKeyEntity
import com.example.bookservice.repository.entity.PriceEntity
import com.example.bookservice.service.RequestValidation.validate
import com.example.orderservice.domain.kafka.client.model.*
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.data.web.PagedModel
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class BookService(
    private val bookRepository: BookRepository,
    private val idempotencyKeyRepository: IdempotencyKeyRepository
) {

    companion object {
        private val logger = KotlinLogging.logger(BookService::class.java.name)
    }

    @Transactional(readOnly = true)
    fun getBooks(bookRequestParams: BookRequestParams, request: BookSearchRequest?): PagedModel<Book> =
        bookRepository.findAll(searchSpec(request), bookRequestParams.toPageable()).toPagedModel()

    @Transactional(readOnly = true)
    fun getBooksByIds(request: BooksFilterRequest): List<Book> =
        bookRepository.findAllById(request.validate().bookIds).map { it.toModel() }

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
        if (key?.bookId != null) {
            throw DuplicateRequestException(key.idempotencyKey, key.bookId)
        }
        val savedBook = bookRepository.save(request.toEntity())
        idempotencyKeyRepository.save(IdempotencyKeyEntity(idempotencyKey, savedBook.id, null))
        return savedBook.toModel()
    }

    @Retryable
    @Transactional
    fun updateBook(bookId: UUID, request: UpdateBookRequest): Book {
        val bookEntity = bookRepository.findByIdWithPessimisticWrite(bookId) ?: throw BookNotFoundException(bookId)
        return bookEntity.apply {
            releaseDate = request.releaseDate
            quantity = request.quantity
            price = request.price?.toEntity() ?: PriceEntity(value = null, currency = null)
        }.toModel()
    }

    @Retryable
    @Transactional
    fun processEvent(event: DomainEvent) {
        when (event) {
            is OrderCreatedEvent -> processCreatedOrder(event.data)
            is OrderUpdatedEvent -> processUpdatedOrder(event.data)
        }
    }

    private fun processCreatedOrder(order: Order) {
        order.items.forEach { book ->
            val bookEntity = bookRepository.findByIdWithPessimisticWrite(book.id)
            if (bookEntity != null) {
                bookEntity.quantity -= book.quantity
            } else {
                logger.error { "Book with id=${book.id} is not found to decrease the quantity by ${book.quantity}" }
            }
        }
    }

    private fun processUpdatedOrder(order: Order) {
        if (order.status == Status.DECLINED || order.status == Status.CANCELLED) {
            order.items.forEach { book ->
                val bookEntity = bookRepository.findByIdWithPessimisticWrite(book.id)
                if (bookEntity != null) {
                    bookEntity.quantity += book.quantity
                } else {
                    logger.error { "Book with id=${book.id} is not found to increase the quantity by ${book.quantity}" }
                }
            }
        }
    }
}

private object RequestValidation {

    private const val MAX_FILTER_SIZE = 100

    fun BooksFilterRequest.validate(): BooksFilterRequest {
        val filterSize = this.bookIds.size
        if (filterSize > MAX_FILTER_SIZE)
            throw RequestValidationException("Book filter size is $filterSize, but max allowed size is $MAX_FILTER_SIZE")
        else return this
    }
}