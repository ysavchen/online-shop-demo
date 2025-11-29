package com.example.online.shop.model

import com.example.online.shop.model.validation.ModelValidationException
import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertContains

class ReviewTextTests {

    private val randomString = RandomStringUtils.insecure()
    private val minLength = 1
    private val maxLength = 5000

    @Test
    fun `valid reviewText`() {
        val reviewTextRange = minLength..maxLength
        val minReviewText = randomString.nextAlphanumeric(minLength)
        val randomReviewText = randomString.nextAlphabetic(reviewTextRange.random())
        val maxReviewText = randomString.nextAlphabetic(maxLength)

        listOf(minReviewText, randomReviewText, maxReviewText).forEach { ReviewText.valueOf(it) }
    }

    @Test
    fun `invalid reviewText length`() {
        val reviewText = randomString.nextAlphabetic(maxLength + 1)
        val exception = assertThrows<ModelValidationException> { ReviewText.valueOf(reviewText) }
        assertContains(exception.message!!, "invalid reviewText", true)
    }

    @Test
    fun `invalid blank description`() {
        val emptyReviewText = ""
        val blankReviewText = " "
        listOf(emptyReviewText, blankReviewText).forEach {
            val exception = assertThrows<ModelValidationException> { ReviewText.valueOf(it) }
            assertContains(exception.message!!, "invalid reviewText", true)
        }
    }
}