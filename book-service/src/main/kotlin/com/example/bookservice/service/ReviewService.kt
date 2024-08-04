package com.example.bookservice.service

import com.example.bookservice.api.rest.ReviewNotFoundException
import com.example.bookservice.api.rest.model.Review
import com.example.bookservice.api.rest.ReviewRequestParams
import com.example.bookservice.api.rest.model.ReviewSearchRequest
import com.example.bookservice.mapping.RequestMapper.toPageable
import com.example.bookservice.mapping.ReviewMapper.toModel
import com.example.bookservice.mapping.ReviewMapper.toPagedModel
import com.example.bookservice.repository.ReviewRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.data.web.PagedModel
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class ReviewService(private val reviewRepository: ReviewRepository) {

    @Transactional(readOnly = true)
    fun getReviews(reviewRequestParams: ReviewRequestParams, request: ReviewSearchRequest): PagedModel<Review> =
        reviewRepository.findAll(reviewRequestParams.toPageable()).toPagedModel()

    @Transactional(readOnly = true)
    fun getReviewById(reviewId: UUID): Review = reviewRepository.findByIdOrNull(reviewId)?.toModel()
        ?: throw ReviewNotFoundException(reviewId)

}