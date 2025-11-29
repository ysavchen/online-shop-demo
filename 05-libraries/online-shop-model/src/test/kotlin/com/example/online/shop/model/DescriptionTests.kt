package com.example.online.shop.model

import com.example.online.shop.model.validation.ModelValidationException
import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertContains

class DescriptionTests {

    private val randomString = RandomStringUtils.insecure()
    private val minLength = 1
    private val maxLength = 5000

    @Test
    fun `valid description`() {
        val descriptionRange = minLength..maxLength
        val minDescription = randomString.nextAlphanumeric(minLength)
        val randomDescription = randomString.nextAlphabetic(descriptionRange.random())
        val maxDescription = randomString.nextAlphabetic(maxLength)

        listOf(minDescription, randomDescription, maxDescription).forEach { Description.valueOf(it) }
    }

    @Test
    fun `invalid description length`() {
        val description = randomString.nextAlphabetic(maxLength + 1)
        val exception = assertThrows<ModelValidationException> { Description.valueOf(description) }
        assertContains(exception.message!!, "invalid description", true)
    }

    @Test
    fun `invalid blank description`() {
        val emptyDescription = ""
        val blankDescription = " "
        listOf(emptyDescription, blankDescription).forEach {
            val exception = assertThrows<ModelValidationException> { Description.valueOf(it) }
            assertContains(exception.message!!, "invalid description", true)
        }
    }
}