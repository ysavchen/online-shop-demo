package com.example.online.shop.model

import com.example.online.shop.model.validation.ModelValidationException
import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertContains

class AuthorTests {

    private val randomString = RandomStringUtils.insecure()
    private val minLength = 1
    private val maxLength = 70

    @Test
    fun `valid author`() {
        val authorRange = minLength..maxLength
        val minAuthor = randomString.nextAlphabetic(minLength)
        val randomAuthor = randomString.nextAlphabetic(authorRange.random())
        val maxAuthor = randomString.nextAlphabetic(maxLength)

        listOf(minAuthor, randomAuthor, maxAuthor).forEach { Author.valueOf(it) }
    }

    @Test
    fun `invalid author length`() {
        val author = randomString.nextAlphabetic(maxLength + 1)
        val exception = assertThrows<ModelValidationException> { Author.valueOf(author) }
        assertContains(exception.message!!, "length", true)
    }

    @Test
    fun `invalid blank author`() {
        val emptyAuthor = ""
        val blankAuthor = " "
        listOf(emptyAuthor, blankAuthor).forEach {
            val exception = assertThrows<ModelValidationException> { Author.valueOf(it) }
            assertContains(exception.message!!, "blank", true)
        }
    }
}