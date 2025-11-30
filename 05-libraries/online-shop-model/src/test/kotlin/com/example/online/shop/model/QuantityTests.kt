package com.example.online.shop.model

import com.example.online.shop.model.QuantityUtils.MAX_QUANTITY
import com.example.online.shop.model.QuantityUtils.MIN_QUANTITY
import com.example.online.shop.model.validation.ModelValidationException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.random.Random
import kotlin.test.assertContains

class QuantityTests {

    @Test
    fun `valid quantity`() {
        val randomQuantity = Random.nextInt(MIN_QUANTITY, MAX_QUANTITY)

        listOf(MIN_QUANTITY, randomQuantity, MAX_QUANTITY).forEach { Quantity.valueOf(it) }
    }

    @Test
    fun `invalid quantity`() {
        val negativeQuantity = MIN_QUANTITY - 1
        val overMaxQuantity = MAX_QUANTITY + 1

        listOf(negativeQuantity, overMaxQuantity).forEach {
            val exception = assertThrows<ModelValidationException> { Quantity.valueOf(it) }
            assertContains(exception.message!!, "invalid quantity", true)
        }
    }
}