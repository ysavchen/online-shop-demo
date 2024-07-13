package com.example.bookservice.api.rest

import com.example.bookservice.api.rest.RestCompanion.BASE_PATH_V1
import com.example.bookservice.api.rest.model.Review
import com.example.bookservice.api.rest.model.ReviewRequestParams
import com.example.bookservice.api.rest.model.ReviewSearchRequest
import com.example.bookservice.service.ReviewService
import org.springframework.data.web.PagedModel
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping(BASE_PATH_V1, produces = [MediaType.APPLICATION_JSON_VALUE])
class ReviewController(private val reviewService: ReviewService) {

    @PostMapping("/reviews", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun bookReviews(
        reviewRequestParams: ReviewRequestParams,
        @RequestBody request: ReviewSearchRequest
    ): PagedModel<Review> = reviewService.getReviews(reviewRequestParams, request)

    @GetMapping("/reviews/{reviewId}")
    fun reviewById(@PathVariable("reviewId") reviewId: UUID): Review = reviewService.getReviewById(reviewId)

}