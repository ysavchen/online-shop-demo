package com.example.online.shop.model

import com.example.online.shop.model.CityUtils.MAX_CITY_LENGTH
import com.example.online.shop.model.CityUtils.MIN_CITY_LENGTH
import com.example.online.shop.model.validation.ModelValidationException
import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertContains

class CityTests {

    private val randomString = RandomStringUtils.insecure()

    @Test
    fun `valid city`() {
        val cityRange = MIN_CITY_LENGTH..MAX_CITY_LENGTH
        val minCity = randomString.nextAlphabetic(MIN_CITY_LENGTH)
        val randomCity = randomString.nextAlphabetic(cityRange.random())
        val maxCity = randomString.nextAlphabetic(MAX_CITY_LENGTH)

        listOf(minCity, randomCity, maxCity).forEach { City.valueOf(it) }
    }

    @Test
    fun `invalid city length`() {
        val city = randomString.nextAlphabetic(MAX_CITY_LENGTH + 1)
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