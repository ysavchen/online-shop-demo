package com.example.online.shop.model

import com.example.online.shop.model.validation.ModelValidationException
import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertContains

class CountryTests {

    private val randomString = RandomStringUtils.insecure()
    private val minLength = 1
    private val maxLength = 100

    @Test
    fun `valid country`() {
        val countryRange = minLength..maxLength
        val minCountry = randomString.nextAlphabetic(minLength)
        val randomCountry = randomString.nextAlphabetic(countryRange.random())
        val maxCountry = randomString.nextAlphabetic(maxLength)

        listOf(minCountry, randomCountry, maxCountry).forEach { Country.valueOf(it) }
    }

    @Test
    fun `invalid country length`() {
        val country = randomString.nextAlphabetic(maxLength + 1)
        val exception = assertThrows<ModelValidationException> { Country.valueOf(country) }
        assertContains(exception.message!!, "invalid country", true)
    }

    @Test
    fun `invalid blank country`() {
        val emptyCountry = ""
        val blankCountry = " "
        listOf(emptyCountry, blankCountry).forEach {
            val exception = assertThrows<ModelValidationException> { Country.valueOf(it) }
            assertContains(exception.message!!, "invalid country", true)
        }
    }
}