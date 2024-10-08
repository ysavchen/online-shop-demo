package com.example.bookservice.api.rest

import com.example.bookservice.api.rest.model.*
import com.example.bookservice.service.ReviewService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.data.web.PagedModel
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1", produces = [MediaType.APPLICATION_JSON_VALUE])
class ReviewController(private val reviewService: ReviewService) {

    @Operation(summary = "Search reviews")
    @PostMapping("/reviews/search", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun reviews(
        reviewRequestParams: ReviewRequestParams,
        @RequestBody request: ReviewSearchRequest
    ): PagedModel<Review> = reviewService.getReviews(reviewRequestParams, request)

    @Operation(summary = "Get review by id")
    @GetMapping("/reviews/{reviewId}")
    fun reviewById(@PathVariable("reviewId") reviewId: UUID): Review = reviewService.getReviewById(reviewId)

    @Operation(summary = "Create review")
    @PostMapping("/reviews", consumes = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun createReview(
        @RequestHeader(IDEMPOTENCY_KEY) idempotencyKey: UUID,
        @RequestBody request: CreateReviewRequest
    ): Review = reviewService.createReview(idempotencyKey, request)
}