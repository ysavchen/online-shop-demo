package com.example.online.shop.model

import com.example.online.shop.model.AuthorUtils.MAX_LENGTH
import com.example.online.shop.model.AuthorUtils.MIN_LENGTH
import com.example.online.shop.model.validation.ModelValidationException
import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertContains

class AuthorTests {

    private val randomString = RandomStringUtils.insecure()

    @Test
    fun `valid author`() {
        val authorRange = MIN_LENGTH..MAX_LENGTH
        val minAuthor = randomString.nextAlphabetic(MIN_LENGTH)
        val randomAuthor = randomString.nextAlphabetic(authorRange.random())
        val maxAuthor = randomString.nextAlphabetic(MAX_LENGTH)

        listOf(minAuthor, randomAuthor, maxAuthor).forEach { Author.valueOf(it) }
    }

    @Test
    fun `invalid author length`() {
        val author = randomString.nextAlphabetic(MAX_LENGTH + 1)
        val exception = assertThrows<ModelValidationException> { Author.valueOf(author) }
        assertContains(exception.message!!, "invalid author", true)
    }

    @Test
    fun `invalid blank author`() {
        val emptyAuthor = ""
        val blankAuthor = " "
        listOf(emptyAuthor, blankAuthor).forEach {
            val exception = assertThrows<ModelValidationException> { Author.valueOf(it) }
            assertContains(exception.message!!, "invalid author", true)
        }
    }
}