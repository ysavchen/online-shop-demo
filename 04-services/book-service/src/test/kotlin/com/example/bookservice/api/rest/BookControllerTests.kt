package com.example.bookservice.api.rest

import com.example.bookservice.api.rest.model.*
import com.example.bookservice.mapping.BookMapper.toEntity
import com.example.bookservice.mapping.BookMapper.toModel
import com.example.bookservice.repository.BookRepository
import com.example.bookservice.repository.ReviewRepository
import com.example.bookservice.test.BookTestData.bookEntity
import com.example.bookservice.test.BookTestData.createBookRequest
import com.example.bookservice.test.BookTestData.updateBookRequest
import com.example.bookservice.test.IntegrationTest
import com.example.bookservice.test.nextValue
import com.example.online.shop.model.Isbn
import com.example.online.shop.model.SearchQuery
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.core.StringContains.containsString
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post
import java.util.*

@IntegrationTest
class BookControllerTests(
    @Autowired val mockMvc: MockMvc,
    @Autowired val bookRepository: BookRepository,
    @Autowired val reviewRepository: ReviewRepository,
    @Autowired val objectMapper: ObjectMapper
) {

    @BeforeEach
    fun beforeEach() {
        reviewRepository.deleteAll()
        bookRepository.deleteAll()
    }

    @Test
    fun `search books by title`() {
        val book = bookRepository.save(bookEntity()).toModel()
        val request = BookSearchRequest(
            query = SearchQuery(book.title.value),
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
            genre = book.genre,
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
    fun `search non-existing book`() {
        val request = BookSearchRequest(
            query = null,
            genre = nextValue<Genre>(),
            minPrice = null,
            maxPrice = null
        )

        mockMvc.post("/api/v1/books/search?page=0") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { string(containsString(""""content":[]""")) }
        }
    }

    @Test
    fun `search books with non-supported sortBy parameter`() {
        val book = bookRepository.save(bookEntity()).toModel()
        val request = BookSearchRequest(
            query = null,
            genre = book.genre,
            minPrice = null,
            maxPrice = null
        )

        mockMvc.post("/api/v1/books/search?page=0&sort_by=invalidSortBy") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isBadRequest() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { string(containsString(ErrorCode.REQUEST_VALIDATION_ERROR.name)) }
        }
    }

    @Test
    fun `search books with non-supported orderBy parameter`() {
        val book = bookRepository.save(bookEntity()).toModel()
        val request = BookSearchRequest(
            query = null,
            genre = book.genre,
            minPrice = null,
            maxPrice = null
        )

        mockMvc.post("/api/v1/books/search?page=0&order_by=invalidOrderBy") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isBadRequest() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { string(containsString(ErrorCode.REQUEST_VALIDATION_ERROR.name)) }
        }
    }

    @Test
    fun `filter books by ids`() {
        val bookOne = bookRepository.save(bookEntity(isbn = Isbn.valueOf("9781783758746"))).toModel()
        val bookTwo = bookRepository.save(bookEntity(isbn = Isbn.valueOf("9780733641541"))).toModel()
        val request = BookFilterRequest(listOf(bookOne.id, bookTwo.id))

        val result = mockMvc.post("/api/v1/books/filter") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }.andReturn()

        val bookOneJson = objectMapper.writeValueAsString(bookOne)
        val bookTwoJson = objectMapper.writeValueAsString(bookTwo)
        assertThat(result.response.contentAsString).contains(bookOneJson, bookTwoJson)
    }

    @Test
    fun `filter books by existing and non-existing ids`() {
        val book = bookRepository.save(bookEntity()).toModel()
        val nonExistingId = UUID.randomUUID()
        val request = BookFilterRequest(listOf(book.id, nonExistingId))

        val result = mockMvc.post("/api/v1/books/filter") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }.andReturn()

        val bookJson = objectMapper.writeValueAsString(book)
        assertThat(result.response.contentAsString).contains(bookJson)
    }

    @Test
    fun `filter books by non-existing ids`() {
        val nonExistingId = UUID.randomUUID()
        val request = BookFilterRequest(listOf(nonExistingId))

        val result = mockMvc.post("/api/v1/books/filter") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }.andReturn()

        assertThat(result.response.contentAsString).isEqualTo("[]")
    }

    @Test
    fun `filter books with exceeded filter size`() {
        val idList = mutableListOf<UUID>().apply {
            repeat(101) { add(UUID.randomUUID()) }
        }
        val request = BookFilterRequest(idList)

        mockMvc.post("/api/v1/books/filter") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isBadRequest() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { string(containsString(ErrorCode.REQUEST_VALIDATION_ERROR.name)) }
        }
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
    fun `get book by non-existing id`() {
        mockMvc.get("/api/v1/books/${UUID.randomUUID()}") {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isNotFound() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { string(containsString(ErrorCode.RESOURCE_NOT_FOUND.name)) }
        }
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

        val result = mockMvc.post("/api/v1/books") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            header(IDEMPOTENCY_KEY, idempotencyKey)
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isCreated() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }.andReturn()

        val createdBook = objectMapper.readValue(result.response.contentAsString, Book::class.java)
        assertThat(createdBook)
            .hasFieldOrProperty("id")
            .hasFieldOrPropertyWithValue("title", request.title.value)
            .hasFieldOrPropertyWithValue("authors", request.authors)
            .hasFieldOrPropertyWithValue("genre", request.genre)
            .hasFieldOrPropertyWithValue("releaseDate", request.releaseDate)
            .hasFieldOrPropertyWithValue("quantity", request.quantity.value)
            .hasFieldOrPropertyWithValue("price", request.price)
    }

    @Test
    fun `duplicate create book request`() {
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
        }

        mockMvc.post("/api/v1/books") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            header(IDEMPOTENCY_KEY, idempotencyKey)
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isConflict() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { string(containsString(ErrorCode.REQUEST_ALREADY_PROCESSED.name)) }
        }
    }

    @Test
    fun `update book`() {
        val book = bookRepository.save(bookEntity()).toModel()
        val request = updateBookRequest()

        mockMvc.patch("/api/v1/books/${book.id}") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }

        val updatedBookEntity = bookRepository.findByIdOrNull(book.id)
        assertThat(updatedBookEntity)
            .hasFieldOrPropertyWithValue("releaseDate", request.releaseDate)
            .hasFieldOrPropertyWithValue("quantity", request.quantity.value)
            .hasFieldOrPropertyWithValue("price", request.price?.toEntity())
    }
}