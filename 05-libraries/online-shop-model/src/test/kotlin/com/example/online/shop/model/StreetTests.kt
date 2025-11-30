package com.example.online.shop.model

import com.example.online.shop.model.StreetUtils.MAX_STREET_LENGTH
import com.example.online.shop.model.StreetUtils.MIN_STREET_LENGTH
import com.example.online.shop.model.validation.ModelValidationException
import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertContains

class StreetTests {

    private val randomString = RandomStringUtils.insecure()

    @Test
    fun `valid street`() {
        val streetRange = MIN_STREET_LENGTH..MAX_STREET_LENGTH
        val minStreet = randomString.nextAlphanumeric(MIN_STREET_LENGTH)
        val randomStreet = randomString.nextAlphanumeric(streetRange.random())
        val maxStreet = randomString.nextAlphanumeric(MAX_STREET_LENGTH)

        listOf(minStreet, randomStreet, maxStreet).forEach { Street.valueOf(it) }
    }

    @Test
    fun `invalid street length`() {
        val street = randomString.nextAlphanumeric(MAX_STREET_LENGTH + 1)
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