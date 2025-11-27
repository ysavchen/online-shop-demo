package com.example.online.shop.model

import com.example.online.shop.model.test.XssTestData.xssScripts
import com.example.online.shop.model.validation.ModelValidationException
import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertContains

class TitleTests {

    private val randomString = RandomStringUtils.insecure()
    private val minLength = 1
    private val maxLength = 150

    @Test
    fun `valid title`() {
        val titleRange = minLength..maxLength
        val minTitle = randomString.nextAlphabetic(minLength)
        val title = randomString.nextAlphabetic(titleRange.random())
        val maxTitle = randomString.nextAlphabetic(minLength)

        listOf(minTitle, title, maxTitle)
            .forEach { Title.valueOf(it) }
    }

    @Test
    fun `invalid title length`() {
        val title = randomString.nextAlphabetic(maxLength + 1)
        val exception = assertThrows<ModelValidationException> { Title.valueOf(title) }
        assertContains(exception.message!!, "length", true)
    }

    @Test
    fun `invalid blank title`() {
        val emptyTitle = ""
        val blankTitle = " "
        listOf(emptyTitle, blankTitle).forEach {
            val exception = assertThrows<ModelValidationException> { Title.valueOf(it) }
            assertContains(exception.message!!, "blank", true)
        }
    }

    @Test
    fun `invalid title with xss-script`() {
        xssScripts().forEach {
            val exception = assertThrows<ModelValidationException> { Title.valueOf(it) }
            assertContains(exception.message!!, "must not contain scripts", true)
        }
    }
}