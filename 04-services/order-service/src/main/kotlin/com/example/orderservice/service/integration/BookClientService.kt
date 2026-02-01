package com.example.orderservice.service.integration

import com.example.bookservice.rest.client.BookServiceRestClient
import com.example.orderservice.api.rest.OrderItemValidationException
import com.example.orderservice.api.rest.model.OrderItem
import com.example.orderservice.config.ApplicationProperties
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service

@Service
class BookClientService(
    private val restClient: BookServiceRestClient,
    private val appProperties: ApplicationProperties
) {

    fun validateBooks(items: Collection<OrderItem>) {
        if (!appProperties.features.bookValidation.enabled) {
            return
        }

        runBlocking {
            items.forEach { item ->
                launch {
                    val book = restClient.getBookById(item.id)
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
}