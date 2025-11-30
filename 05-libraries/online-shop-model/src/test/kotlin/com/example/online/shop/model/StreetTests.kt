package com.example.online.shop.model

import com.example.online.shop.model.StreetUtils.MAX_LENGTH
import com.example.online.shop.model.StreetUtils.MIN_LENGTH
import com.example.online.shop.model.validation.ModelValidationException
import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertContains

class StreetTests {

    private val randomString = RandomStringUtils.insecure()

    @Test
    fun `valid street`() {
        val streetRange = MIN_LENGTH..MAX_LENGTH
        val minStreet = randomString.nextAlphabetic(MIN_LENGTH)
        val randomStreet = randomString.nextAlphabetic(streetRange.random())
        val maxStreet = randomString.nextAlphabetic(MAX_LENGTH)

        listOf(minStreet, randomStreet, maxStreet).forEach { Street.valueOf(it) }
    }

    @Test
    fun `invalid street length`() {
        val street = randomString.nextAlphabetic(MAX_LENGTH + 1)
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