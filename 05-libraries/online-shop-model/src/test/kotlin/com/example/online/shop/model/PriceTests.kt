package com.example.online.shop.model

import com.example.online.shop.model.validation.ModelValidationException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import kotlin.random.Random
import kotlin.test.assertContains

class PriceTests {

    private val minPrice = BigDecimal.valueOf(0.0)
    private val maxPrice = BigDecimal.valueOf(9999999.99)

    @Test
    fun `valid price`() {
        val randomPrice = Random.nextDouble(minPrice.toDouble(), maxPrice.toDouble()).toBigDecimal()

        listOf(minPrice, randomPrice, maxPrice).forEach { Price.valueOf(it) }
    }

    @Test
    fun `invalid price`() {
        val negativePrice = minPrice.minus(BigDecimal.valueOf(0.1))
        val overMaxPrice = maxPrice.plus(BigDecimal.valueOf(0.1))

        listOf(negativePrice, overMaxPrice).forEach {
            val exception = assertThrows<ModelValidationException> { Price.valueOf(it) }
            assertContains(exception.message!!, "invalid price", true)
        }
    }
}