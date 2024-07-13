package com.example.bookservice.api.rest

import com.example.bookservice.api.rest.RestCompanion.BASE_PATH_V1
import com.example.bookservice.api.rest.model.Review
import com.example.bookservice.api.rest.model.ReviewRequestParams
import com.example.bookservice.api.rest.model.ReviewSearchRequest
import org.springframework.data.web.PagedModel
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping(BASE_PATH_V1, produces = [MediaType.APPLICATION_JSON_VALUE])
class ReviewController {

    @PostMapping("/reviews")
    fun bookReviews(
        reviewRequestParams: ReviewRequestParams,
        @RequestBody request: ReviewSearchRequest
    ): PagedModel<Review> = TODO()

    @GetMapping("/reviews/{reviewId}")
    fun reviewById(@PathVariable("reviewId") reviewId: UUID): Review = TODO()

}