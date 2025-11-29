package com.example.online.shop.model

import com.example.online.shop.model.validation.ModelValidationException
import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertContains

class CityTests {

    private val randomString = RandomStringUtils.insecure()
    private val minLength = 1
    private val maxLength = 100

    @Test
    fun `valid city`() {
        val cityRange = minLength..maxLength
        val minCity = randomString.nextAlphabetic(minLength)
        val randomCity = randomString.nextAlphabetic(cityRange.random())
        val maxCity = randomString.nextAlphabetic(maxLength)

        listOf(minCity, randomCity, maxCity).forEach { City.valueOf(it) }
    }

    @Test
    fun `invalid city length`() {
        val city = randomString.nextAlphabetic(maxLength + 1)
        val exception = assertThrows<ModelValidationException> { City.valueOf(city) }
        assertContains(exception.message!!, "invalid city", true)
    }

    @Test
    fun `invalid blank city`() {
        val emptyCity = ""
        val blankCity = " "
        listOf(emptyCity, blankCity).forEach {
            val exception = assertThrows<ModelValidationException> { City.valueOf(it) }
            assertContains(exception.message!!, "invalid city", true)
        }
    }
}