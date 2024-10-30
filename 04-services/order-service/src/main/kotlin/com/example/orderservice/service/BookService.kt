package com.example.orderservice.service

import com.example.bookservice.rest.client.BookServiceClient
import com.example.orderservice.api.rest.OrderItemValidationException
import com.example.orderservice.api.rest.model.OrderItem
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service

@Service
class BookService(private val bookServiceClient: BookServiceClient) {

    fun validateBooks(items: Collection<OrderItem>) {
        items.forEach { item ->
            runBlocking {
                val book = bookServiceClient.getBookById(item.id)
                if (item.quantity > book.quantity) {
                    throw OrderItemValidationException("Insufficient quantity of books (bookId=${item.id}): ordered=${item.quantity}, available in store=${book.quantity}")
                }
                if (item.price.value != book.price?.value) {
                    throw OrderItemValidationException("Inconsistent book price (bookId=${item.id}): in order=${item.price.value}, in store=${book.price?.value}")
                }
            }
        }
    }
}