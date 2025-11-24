package com.example.online.shop.model

import com.example.online.shop.model.validation.ModelValidationException
import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertContains

class AuthorTests {

    private val randomString = RandomStringUtils.insecure()

    @Test
    fun `valid author`() {
        val authorRange = 1..70
        val minAuthor = randomString.nextAlphabetic(1)
        val author = randomString.nextAlphabetic(authorRange.random())
        val maxAuthor = randomString.nextAlphabetic(70)

        listOf(minAuthor, author, maxAuthor)
            .forEach { Author.valueOf(it) }
    }

    @Test
    fun `invalid author length`() {
        val author = randomString.nextAlphabetic(71)
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