package com.example.bookservice.test

import com.example.bookservice.api.rest.model.CreateReviewRequest
import com.example.bookservice.repository.entity.ReviewEntity
import com.example.online.shop.model.Title
import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import java.util.*

object ReviewTestData {

    fun createReviewRequest(bookId: UUID) = CreateReviewRequest(
        title = Title.valueOf(randomAlphabetic(15)),
        reviewText = randomAlphabetic(25),
        author = randomAlphabetic(10),
        rating = randomRating(),
        bookId = bookId
    )

    fun reviewEntity(bookFk: UUID) = ReviewEntity(
        title = Title.valueOf(randomAlphabetic(15)),
        reviewText = randomAlphabetic(25),
        author = randomAlphabetic(10),
        rating = randomRating(),
        bookFk = bookFk
    )
}