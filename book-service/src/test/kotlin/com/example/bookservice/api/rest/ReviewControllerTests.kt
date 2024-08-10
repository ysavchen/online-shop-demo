package com.example.bookservice.api.rest

import com.example.bookservice.api.rest.model.ErrorCode
import com.example.bookservice.api.rest.model.ReviewSearchRequest
import com.example.bookservice.mapping.ReviewMapper.toModel
import com.example.bookservice.repository.BookRepository
import com.example.bookservice.repository.ReviewRepository
import com.example.bookservice.test.BookTestData.bookEntity
import com.example.bookservice.test.IntegrationTest
import com.example.bookservice.test.ReviewTestData.reviewEntity
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
class ReviewControllerTests {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var bookRepository: BookRepository

    @Autowired
    lateinit var reviewRepository: ReviewRepository

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun `search reviews by bookId`() {
        val bookEntity = bookRepository.save(bookEntity())
        val review = reviewRepository.save(reviewEntity(bookEntity.id!!)).toModel()
        val request = ReviewSearchRequest(bookEntity.id!!)

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
    fun `search with non-supported sorting`() {
        val bookEntity = bookRepository.save(bookEntity())
        reviewRepository.save(reviewEntity(bookEntity.id!!)).toModel()
        val request = ReviewSearchRequest(bookEntity.id!!)

        mockMvc.post("/api/v1/reviews/search?page=0&sortBy=language") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isBadRequest() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { string(containsString(ErrorCode.SORTING_CATEGORY_NOT_SUPPORTED.name)) }
        }
    }

    @Test
    fun `get review by id`() {
        val bookEntity = bookRepository.save(bookEntity())
        val review = reviewRepository.save(reviewEntity(bookEntity.id!!)).toModel()

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

}