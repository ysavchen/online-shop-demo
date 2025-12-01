package com.example.bookservice.api.rest

import com.example.bookservice.api.rest.model.ErrorCode
import com.example.bookservice.api.rest.model.IDEMPOTENCY_KEY
import com.example.bookservice.api.rest.model.Review
import com.example.bookservice.api.rest.model.ReviewSearchRequest
import com.example.bookservice.mapping.BookMapper.toModel
import com.example.bookservice.mapping.ReviewMapper.toModel
import com.example.bookservice.repository.BookRepository
import com.example.bookservice.repository.ReviewRepository
import com.example.bookservice.test.BookTestData.bookEntity
import com.example.bookservice.test.IntegrationTest
import com.example.bookservice.test.ReviewTestData.createReviewRequest
import com.example.bookservice.test.ReviewTestData.reviewEntity
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.core.StringContains.containsString
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.util.*

@IntegrationTest
class ReviewControllerTests(
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
    fun `search reviews by bookId`() {
        val book = bookRepository.save(bookEntity()).toModel()
        val review = reviewRepository.save(reviewEntity(book.id)).toModel()
        val request = ReviewSearchRequest(book.id)

        val result = mockMvc.post("/api/v1/reviews/search?page=0") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }.andReturn()

        val expectedReview = objectMapper.writeValueAsString(review)
        assertThat(result.response.contentAsString).contains(expectedReview)
    }

    @Test
    fun `search reviews by non-existing bookId`() {
        val request = ReviewSearchRequest(UUID.randomUUID())

        mockMvc.post("/api/v1/reviews/search?page=0") {
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
    fun `search reviews with non-supported sortBy parameter`() {
        val book = bookRepository.save(bookEntity()).toModel()
        reviewRepository.save(reviewEntity(book.id)).toModel()
        val request = ReviewSearchRequest(book.id)

        mockMvc.post("/api/v1/reviews/search?page=0&sort_by=invalidSortBy") {
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
    fun `search reviews with non-supported orderBy parameter`() {
        val book = bookRepository.save(bookEntity()).toModel()
        reviewRepository.save(reviewEntity(book.id)).toModel()
        val request = ReviewSearchRequest(book.id)

        mockMvc.post("/api/v1/reviews/search?page=0&order_by=invalidOrderBy") {
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
    fun `get review by id`() {
        val book = bookRepository.save(bookEntity()).toModel()
        val review = reviewRepository.save(reviewEntity(book.id)).toModel()

        val result = mockMvc.get("/api/v1/reviews/${review.id}") {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }.andReturn()

        val expectedReview = objectMapper.writeValueAsString(review)
        assertThat(result.response.contentAsString).contains(expectedReview)
    }

    @Test
    fun `get review by non-existing id`() {
        mockMvc.get("/api/v1/reviews/${UUID.randomUUID()}") {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isNotFound() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { string(containsString(ErrorCode.RESOURCE_NOT_FOUND.name)) }
        }
    }

    @Test
    fun `create review`() {
        val book = bookRepository.save(bookEntity()).toModel()
        val request = createReviewRequest(book.id)
        val idempotencyKey = UUID.randomUUID()

        val result = mockMvc.post("/api/v1/reviews") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            header(IDEMPOTENCY_KEY, idempotencyKey)
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isCreated() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }.andReturn()


        val createdReview = objectMapper.readValue(result.response.contentAsString, Review::class.java)
        assertThat(createdReview)
            .hasFieldOrProperty("id")
            .hasFieldOrPropertyWithValue("title", request.title?.value)
            .hasFieldOrPropertyWithValue("reviewText", request.reviewText?.value)
            .hasFieldOrPropertyWithValue("author", request.author.value)
            .hasFieldOrPropertyWithValue("rating", request.rating.value)
            .hasFieldOrPropertyWithValue("bookId", request.bookId)
    }

    @Test
    fun `duplicate create review request`() {
        val book = bookRepository.save(bookEntity()).toModel()
        val request = createReviewRequest(book.id)
        val idempotencyKey = UUID.randomUUID()

        mockMvc.post("/api/v1/reviews") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            header(IDEMPOTENCY_KEY, idempotencyKey)
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isCreated() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }

        mockMvc.post("/api/v1/reviews") {
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
}