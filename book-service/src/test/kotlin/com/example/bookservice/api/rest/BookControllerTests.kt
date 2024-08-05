package com.example.bookservice.api.rest

import com.example.bookservice.api.rest.model.BookSearchRequest
import com.example.bookservice.mapping.BookMapper.toModel
import com.example.bookservice.repository.BookRepository
import com.example.bookservice.test.IntegrationTest
import com.example.bookservice.test.TestData.bookEntity
import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.core.StringContains.containsString
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@IntegrationTest
class BookControllerTests {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var bookRepository: BookRepository

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun `search books by title`() {
        val bookEntity = bookRepository.save(bookEntity())
        val searchRequest = BookSearchRequest(
            query = bookEntity.title,
            genre = null,
            minPrice = null,
            maxPrice = null
        )

        mockMvc.post("/api/v1/books/search?page=0") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(searchRequest)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { string(containsString(bookEntity.id.toString())) }
            content { string(containsString(bookEntity.title)) }
        }
    }

    @Test
    fun `search books by genre`() {
        val bookEntity = bookRepository.save(bookEntity())
        val searchRequest = BookSearchRequest(
            query = null,
            genre = bookEntity.genre.name.lowercase(),
            minPrice = null,
            maxPrice = null
        )

        mockMvc.post("/api/v1/books/search?page=0") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(searchRequest)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { string(containsString(bookEntity.id.toString())) }
            content { string(containsString(bookEntity.title)) }
        }
    }

    @Test
    fun `get book by id`() {
        val bookEntity = bookRepository.save(bookEntity())

        mockMvc.get("/api/v1/books/${bookEntity.id}") {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { json(objectMapper.writeValueAsString(bookEntity.toModel())) }
        }
    }

}