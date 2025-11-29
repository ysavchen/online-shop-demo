package com.example.online.shop.model

import com.example.online.shop.model.validation.ModelValidationException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import kotlin.random.Random
import kotlin.test.assertContains

class RatingTests {

    private val minRating = BigDecimal.valueOf(1.0)
    private val maxRating = BigDecimal.valueOf(5.0)

    @Test
    fun `valid rating`() {
        val randomRating = Random.nextDouble(minRating.toDouble(), maxRating.toDouble()).toBigDecimal()

        listOf(minRating, randomRating, maxRating).forEach { Rating.valueOf(it) }
    }

    @Test
    fun `invalid rating`() {
        val negativeRating = minRating.minus(BigDecimal.valueOf(1.1))
        val zeroRating = BigDecimal.valueOf(0.0)
        val overMaxRating = maxRating.plus(BigDecimal.valueOf(0.1))

        listOf(negativeRating, zeroRating, overMaxRating).forEach {
            val exception = assertThrows<ModelValidationException> { Rating.valueOf(it) }
            assertContains(exception.message!!, "invalid rating", true)
        }
    }
}