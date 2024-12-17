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
import com.example.bookservice.repository.ProcessedMessageRepository
import com.example.bookservice.repository.ProcessedRequestRepository
import com.example.bookservice.repository.entity.PriceEntity
import com.example.bookservice.repository.entity.ProcessedMessageEntity
import com.example.bookservice.repository.entity.ProcessedRequestEntity
import com.example.bookservice.repository.entity.ResourceTypeEntity.BOOK
import com.example.bookservice.repository.entity.ResourceTypeEntity.ORDER
import com.example.bookservice.service.RequestValidation.validate
import com.example.orderservice.domain.kafka.client.model.*
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.persistence.LockTimeoutException
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.data.repository.findByIdOrNull
import org.springframework.data.web.PagedModel
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class BookService(
    private val bookRepository: BookRepository,
    private val requestRepository: ProcessedRequestRepository,
    private val messageRepository: ProcessedMessageRepository
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
        val processedRequest = requestRepository.findByIdOrNull(idempotencyKey)
        if (processedRequest != null) {
            throw DuplicateRequestException(
                idempotencyKey = processedRequest.idempotencyKey,
                resourceId = processedRequest.resourceId,
                resource = processedRequest.resourceType.name.lowercase()
            )
        }
        val book = bookRepository.save(request.toEntity()).toModel()
        requestRepository.save(ProcessedRequestEntity(idempotencyKey, book.id, BOOK))
        return book
    }

    @Retryable(retryFor = [LockTimeoutException::class])
    @Transactional
    fun updateBook(bookId: UUID, request: UpdateBookRequest): Book {
        val bookEntity = bookRepository.findByIdWithPessimisticWrite(bookId) ?: throw BookNotFoundException(bookId)
        return bookEntity.apply {
            releaseDate = request.releaseDate
            quantity = request.quantity
            price = request.price?.toEntity() ?: PriceEntity(value = null, currency = null)
        }.toModel()
    }

    @Retryable(retryFor = [LockTimeoutException::class])
    @Transactional
    fun processMessage(message: ConsumerRecord<UUID, DomainEvent>) {
        val processedMessage = messageRepository.findByIdOrNull(message.key())
        if (processedMessage != null) {
            logger.debug { "Duplicate message with key=${processedMessage.messageKey}, message already processed" }
            return
        }

        val order = when (val event = message.value()) {
            is OrderCreatedEvent -> processCreatedOrder(event.data)
            is OrderUpdatedEvent -> processUpdatedOrder(event.data)
        }
        messageRepository.save(ProcessedMessageEntity(message.key(), order.id, ORDER))
    }

    private fun processCreatedOrder(order: Order): Order {
        for (item in order.items) {
            if (item.category != ItemCategory.BOOKS) continue
            val bookEntity = bookRepository.findByIdWithPessimisticWrite(item.id)
            if (bookEntity != null) {
                bookEntity.quantity -= item.quantity
            } else {
                logger.error { "Book with id=${item.id} is not found to decrease the quantity by ${item.quantity}" }
            }
        }
        return order
    }

    private fun processUpdatedOrder(order: Order): Order {
        if (order.status == Status.DECLINED || order.status == Status.CANCELLED) {
            for (item in order.items) {
                if (item.category != ItemCategory.BOOKS) continue
                val bookEntity = bookRepository.findByIdWithPessimisticWrite(item.id)
                if (bookEntity != null) {
                    bookEntity.quantity += item.quantity
                } else {
                    logger.error { "Book with id=${item.id} is not found to increase the quantity by ${item.quantity}" }
                }
            }
        }
        return order
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