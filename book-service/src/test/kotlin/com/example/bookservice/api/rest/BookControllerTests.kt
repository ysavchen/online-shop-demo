package com.example.bookservice.api.rest

import com.example.bookservice.api.rest.model.BookSearchRequest
import com.example.bookservice.mapping.BookMapper.toModel
import com.example.bookservice.repository.BookRepository
import com.example.bookservice.test.IntegrationTest
import com.example.bookservice.test.BookTestData.bookEntity
import com.example.bookservice.test.BookTestData.createBookRequest
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.core.StringContains.containsString
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.util.*

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
        val book = bookRepository.save(bookEntity()).toModel()
        val request = BookSearchRequest(
            query = book.title,
            genre = null,
            minPrice = null,
            maxPrice = null
        )

        val result = mockMvc.post("/api/v1/books/search?page=0") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }.andReturn()

        val expectedBook = objectMapper.writeValueAsString(book)
        assertThat(result.response.contentAsString).contains(expectedBook)
    }

    @Test
    fun `search books by genre`() {
        val book = bookRepository.save(bookEntity()).toModel()
        val request = BookSearchRequest(
            query = null,
            genre = book.genre.name.lowercase(),
            minPrice = null,
            maxPrice = null
        )

        val result = mockMvc.post("/api/v1/books/search?page=0") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }.andReturn()

        val expectedBook = objectMapper.writeValueAsString(book)
        assertThat(result.response.contentAsString).contains(expectedBook)
    }

    @Test
    fun `get book by id`() {
        val book = bookRepository.save(bookEntity()).toModel()

        val result = mockMvc.get("/api/v1/books/${book.id}") {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }.andReturn()

        val expectedBook = objectMapper.writeValueAsString(book)
        assertThat(result.response.contentAsString).contains(expectedBook)
    }

    @Test
    fun `get book description`() {
        val bookEntity = bookRepository.save(bookEntity())

        mockMvc.get("/api/v1/books/${bookEntity.id}/description") {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { json("""{"description": "${bookEntity.description}"}""") }
        }
    }

    @Test
    fun `create book`() {
        val request = createBookRequest()
        val idempotencyKey = UUID.randomUUID()

        mockMvc.post("/api/v1/books") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            header(IDEMPOTENCY_KEY, idempotencyKey)
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isCreated() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { string(containsString(request.title)) }
        }
    }

}