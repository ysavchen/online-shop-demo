package com.example.online.shop.model

import com.example.online.shop.model.validation.ModelValidationException
import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertContains

class SearchQueryTests {

    private val randomString = RandomStringUtils.insecure()
    private val minLength = 1
    private val maxLength = 300

    @Test
    fun `valid searchQuery`() {
        val searchQueryRange = minLength..maxLength
        val minSearchQuery = randomString.nextAlphanumeric(minLength)
        val randomSearchQuery = randomString.nextAlphabetic(searchQueryRange.random())
        val maxSearchQuery = randomString.nextAlphabetic(maxLength)

        listOf(minSearchQuery, randomSearchQuery, maxSearchQuery).forEach { SearchQuery.valueOf(it) }
    }

    @Test
    fun `valid blank searchQuery`() {
        val emptySearchQuery = ""
        val blankSearchQuery = " "
        listOf(emptySearchQuery, blankSearchQuery).forEach { SearchQuery.valueOf(it) }
    }

    @Test
    fun `invalid searchQuery length`() {
        val searchQuery = randomString.nextAlphabetic(maxLength + 1)
        val exception = assertThrows<ModelValidationException> { SearchQuery.valueOf(searchQuery) }
        assertContains(exception.message!!, "invalid searchQuery", true)
    }
}