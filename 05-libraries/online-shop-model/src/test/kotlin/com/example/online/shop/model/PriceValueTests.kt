package com.example.online.shop.model

import com.example.online.shop.model.validation.ModelValidationException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import kotlin.random.Random
import kotlin.test.assertContains

class PriceValueTests {

    private val minPriceValue = BigDecimal.valueOf(0.0)
    private val maxPriceValue = BigDecimal.valueOf(9999999.99)

    @Test
    fun `valid priceValue`() {
        val randomPrice = Random.nextDouble(minPriceValue.toDouble(), maxPriceValue.toDouble()).toBigDecimal()

        listOf(minPriceValue, randomPrice, maxPriceValue).forEach { PriceValue.valueOf(it) }
    }

    @Test
    fun `invalid priceValue`() {
        val negativePriceValue = minPriceValue.minus(BigDecimal.valueOf(0.1))
        val overMaxPriceValue = maxPriceValue.plus(BigDecimal.valueOf(0.1))

        listOf(negativePriceValue, overMaxPriceValue).forEach {
            val exception = assertThrows<ModelValidationException> { PriceValue.valueOf(it) }
            assertContains(exception.message!!, "invalid priceValue", true)
        }
    }
}