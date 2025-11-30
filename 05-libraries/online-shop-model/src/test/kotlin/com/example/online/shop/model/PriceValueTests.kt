package com.example.online.shop.model

import com.example.online.shop.model.PriceValueUtils.MAX_PRICE_VALUE
import com.example.online.shop.model.PriceValueUtils.MIN_PRICE_VALUE
import com.example.online.shop.model.validation.ModelValidationException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import kotlin.random.Random
import kotlin.test.assertContains

class PriceValueTests {

    @Test
    fun `valid priceValue`() {
        val randomPrice = Random.nextDouble(MIN_PRICE_VALUE.toDouble(), MAX_PRICE_VALUE.toDouble()).toBigDecimal()

        listOf(MIN_PRICE_VALUE, randomPrice, MAX_PRICE_VALUE).forEach { PriceValue.valueOf(it) }
    }

    @Test
    fun `invalid priceValue`() {
        val negativePriceValue = MIN_PRICE_VALUE.minus(BigDecimal.valueOf(0.1))
        val overMaxPriceValue = MAX_PRICE_VALUE.plus(BigDecimal.valueOf(0.1))

        listOf(negativePriceValue, overMaxPriceValue).forEach {
            val exception = assertThrows<ModelValidationException> { PriceValue.valueOf(it) }
            assertContains(exception.message!!, "invalid priceValue", true)
        }
    }
}