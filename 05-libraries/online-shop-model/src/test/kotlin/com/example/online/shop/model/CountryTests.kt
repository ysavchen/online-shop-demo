package com.example.online.shop.model

import com.example.online.shop.model.CountryUtils.MAX_COUNTRY_LENGTH
import com.example.online.shop.model.CountryUtils.MAX_LENGTH
import com.example.online.shop.model.CountryUtils.MIN_COUNTRY_LENGTH
import com.example.online.shop.model.CountryUtils.MIN_LENGTH
import com.example.online.shop.model.validation.ModelValidationException
import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertContains

class CountryTests {

    private val randomString = RandomStringUtils.insecure()

    @Test
    fun `valid country`() {
        val countryRange = MIN_COUNTRY_LENGTH..MAX_COUNTRY_LENGTH
        val minCountry = randomString.nextAlphabetic(MIN_COUNTRY_LENGTH)
        val randomCountry = randomString.nextAlphabetic(countryRange.random())
        val maxCountry = randomString.nextAlphabetic(MAX_COUNTRY_LENGTH)

        listOf(minCountry, randomCountry, maxCountry).forEach { Country.valueOf(it) }
    }

    @Test
    fun `invalid country length`() {
        val country = randomString.nextAlphabetic(MAX_COUNTRY_LENGTH + 1)
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