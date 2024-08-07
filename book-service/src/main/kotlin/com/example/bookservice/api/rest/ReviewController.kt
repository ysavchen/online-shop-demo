package com.example.bookservice.api.rest

import com.example.bookservice.api.rest.model.CreateReviewRequest
import com.example.bookservice.api.rest.model.Review
import com.example.bookservice.api.rest.model.ReviewSearchRequest
import com.example.bookservice.service.ReviewService
import org.springframework.data.web.PagedModel
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1", produces = [MediaType.APPLICATION_JSON_VALUE])
class ReviewController(private val reviewService: ReviewService) {

    @PostMapping("/reviews/search", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun reviews(
        reviewRequestParams: ReviewRequestParams,
        @RequestBody request: ReviewSearchRequest
    ): PagedModel<Review> = reviewService.getReviews(reviewRequestParams, request)

    @GetMapping("/reviews/{reviewId}")
    fun reviewById(@PathVariable("reviewId") reviewId: UUID): Review = reviewService.getReviewById(reviewId)

    @PostMapping("/reviews", consumes = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun createReview(
        @RequestHeader(IDEMPOTENCY_KEY) idempotencyKey: UUID,
        @RequestBody request: CreateReviewRequest
    ): Review = reviewService.createReview(idempotencyKey, request)
}