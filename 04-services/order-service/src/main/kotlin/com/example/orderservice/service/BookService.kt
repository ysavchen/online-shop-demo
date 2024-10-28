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
                    throw OrderItemValidationException("Order item with id=${item.id}, category=${item.category} is not available in store")
                }
                if (item.price.value != book.price?.value) {
                    throw OrderItemValidationException("Inconsistent book price: order=${item.price.value}, store=${book.price?.value}")
                }
            }
        }
    }
}