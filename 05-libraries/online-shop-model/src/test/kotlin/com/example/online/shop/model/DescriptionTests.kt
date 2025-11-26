package com.example.online.shop.model

import com.example.online.shop.model.validation.ModelValidationException
import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertContains

class DescriptionTests {

    private val randomString = RandomStringUtils.insecure()
    private val minLength = 1
    private val maxTestLength = 1000

    @Test
    fun `valid description`() {
        val descriptionRange = minLength..maxTestLength
        val minDescription = randomString.nextAlphanumeric(minLength)
        val description = randomString.nextAlphabetic(descriptionRange.random())

        listOf(minDescription, description)
            .forEach { Description.valueOf(it) }
    }

    @Test
    fun `invalid blank description`() {
        val emptyDescription = ""
        val blankDescription = " "
        listOf(emptyDescription, blankDescription).forEach {
            val exception = assertThrows<ModelValidationException> { Description.valueOf(it) }
            assertContains(exception.message!!, "blank", true)
        }
    }
}