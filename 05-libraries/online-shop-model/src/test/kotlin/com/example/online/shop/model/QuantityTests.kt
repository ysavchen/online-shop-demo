package com.example.online.shop.model

import com.example.online.shop.model.validation.ModelValidationException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.random.Random
import kotlin.test.assertContains

class QuantityTests {

    private val minQuantity = 0
    private val maxQuantity = Int.MAX_VALUE

    @Test
    fun `valid quantity`() {
        val randomQuantity = Random.nextInt(minQuantity, maxQuantity)

        listOf(minQuantity, randomQuantity, maxQuantity).forEach { Quantity.valueOf(it) }
    }

    @Test
    fun `invalid quantity`() {
        val negativeQuantity = minQuantity - 1
        val overMaxQuantity = maxQuantity + 1

        listOf(negativeQuantity, overMaxQuantity).forEach {
            val exception = assertThrows<ModelValidationException> { Quantity.valueOf(it) }
            assertContains(exception.message!!, "invalid quantity", true)
        }
    }
}