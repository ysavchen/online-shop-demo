package com.example.online.shop.model

import com.example.online.shop.model.RatingUtils.MAX_RATING
import com.example.online.shop.model.RatingUtils.MIN_RATING
import com.example.online.shop.model.validation.ModelValidationException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import kotlin.random.Random
import kotlin.test.assertContains

class RatingTests {

    @Test
    fun `valid rating`() {
        val randomRating = Random.nextDouble(MIN_RATING.toDouble(), MAX_RATING.toDouble()).toBigDecimal()

        listOf(MIN_RATING, randomRating, MAX_RATING).forEach { Rating.valueOf(it) }
    }

    @Test
    fun `invalid rating`() {
        val negativeRating = MIN_RATING.minus(BigDecimal.valueOf(1.1))
        val zeroRating = BigDecimal.valueOf(0.0)
        val overMaxRating = MAX_RATING.plus(BigDecimal.valueOf(0.1))

        listOf(negativeRating, zeroRating, overMaxRating).forEach {
            val exception = assertThrows<ModelValidationException> { Rating.valueOf(it) }
            assertContains(exception.message!!, "invalid rating", true)
        }
    }
}