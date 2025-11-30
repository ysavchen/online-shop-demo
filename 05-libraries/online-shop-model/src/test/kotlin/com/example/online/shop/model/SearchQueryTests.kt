package com.example.online.shop.model

import com.example.online.shop.model.SearchQueryUtils.MAX_LENGTH
import com.example.online.shop.model.SearchQueryUtils.MIN_LENGTH
import com.example.online.shop.model.validation.ModelValidationException
import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertContains

class SearchQueryTests {

    private val randomString = RandomStringUtils.insecure()

    @Test
    fun `valid searchQuery`() {
        val searchQueryRange = MIN_LENGTH..MAX_LENGTH
        val minSearchQuery = randomString.nextAlphanumeric(MIN_LENGTH)
        val randomSearchQuery = randomString.nextAlphabetic(searchQueryRange.random())
        val maxSearchQuery = randomString.nextAlphabetic(MAX_LENGTH)

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
        val searchQuery = randomString.nextAlphabetic(MAX_LENGTH + 1)
        val exception = assertThrows<ModelValidationException> { SearchQuery.valueOf(searchQuery) }
        assertContains(exception.message!!, "invalid searchQuery", true)
    }
}