package com.example.online.shop.model

import com.example.online.shop.model.validation.ModelValidationException
import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertContains

class StreetTests {

    private val randomString = RandomStringUtils.insecure()
    private val minLength = 1
    private val maxLength = 150

    @Test
    fun `valid street`() {
        val streetRange = minLength..maxLength
        val minStreet = randomString.nextAlphabetic(minLength)
        val randomStreet = randomString.nextAlphabetic(streetRange.random())
        val maxStreet = randomString.nextAlphabetic(maxLength)

        listOf(minStreet, randomStreet, maxStreet).forEach { Street.valueOf(it) }
    }

    @Test
    fun `invalid street length`() {
        val street = randomString.nextAlphabetic(maxLength + 1)
        val exception = assertThrows<ModelValidationException> { Street.valueOf(street) }
        assertContains(exception.message!!, "invalid street", true)
    }

    @Test
    fun `invalid blank street`() {
        val emptyStreet = ""
        val blankStreet = " "
        listOf(emptyStreet, blankStreet).forEach {
            val exception = assertThrows<ModelValidationException> { Street.valueOf(it) }
            assertContains(exception.message!!, "invalid street", true)
        }
    }
}