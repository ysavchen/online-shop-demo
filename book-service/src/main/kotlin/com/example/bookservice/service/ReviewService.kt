package com.example.bookservice.service

import com.example.bookservice.api.rest.DuplicateRequestException
import com.example.bookservice.api.rest.ReviewNotFoundException
import com.example.bookservice.api.rest.ReviewRequestParams
import com.example.bookservice.api.rest.model.CreateReviewRequest
import com.example.bookservice.api.rest.model.Review
import com.example.bookservice.api.rest.model.ReviewSearchRequest
import com.example.bookservice.mapping.RequestMapper.toPageable
import com.example.bookservice.mapping.ReviewMapper.toEntity
import com.example.bookservice.mapping.ReviewMapper.toModel
import com.example.bookservice.mapping.ReviewMapper.toPagedModel
import com.example.bookservice.repository.IdempotencyKeyRepository
import com.example.bookservice.repository.ReviewRepository
import com.example.bookservice.repository.ReviewRepository.Companion.searchSpec
import com.example.bookservice.repository.entity.IdempotencyKeyEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.data.web.PagedModel
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class ReviewService(
    private val reviewRepository: ReviewRepository,
    private val idempotencyKeyRepository: IdempotencyKeyRepository
) {

    @Transactional(readOnly = true)
    fun getReviews(reviewRequestParams: ReviewRequestParams, request: ReviewSearchRequest): PagedModel<Review> =
        reviewRepository.findAll(searchSpec(request), reviewRequestParams.toPageable()).toPagedModel()

    @Transactional(readOnly = true)
    fun getReviewById(reviewId: UUID): Review = reviewRepository.findByIdOrNull(reviewId)?.toModel()
        ?: throw ReviewNotFoundException(reviewId)

    @Transactional
    fun createReview(idempotencyKey: UUID, request: CreateReviewRequest): Review {
        val key = idempotencyKeyRepository.findByIdOrNull(idempotencyKey)
        if (key?.reviewId != null) throw DuplicateRequestException(key.idempotencyKey, key.reviewId)
        val savedReview = reviewRepository.save(request.toEntity())
        idempotencyKeyRepository.save(
            IdempotencyKeyEntity(
                idempotencyKey = idempotencyKey,
                bookId = null,
                reviewId = savedReview.id
            )
        )
        return savedReview.toModel()
    }
}