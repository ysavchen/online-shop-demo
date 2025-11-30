package com.example.online.shop.model

import com.example.online.shop.model.TitleUtils.MAX_TITLE_LENGTH
import com.example.online.shop.model.TitleUtils.MIN_TITLE_LENGTH
import com.example.online.shop.model.validation.ModelValidationException
import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertContains

class TitleTests {

    private val randomString = RandomStringUtils.insecure()

    @Test
    fun `valid title`() {
        val titleRange = MIN_TITLE_LENGTH..MAX_TITLE_LENGTH
        val minTitle = randomString.nextAlphabetic(MIN_TITLE_LENGTH)
        val title = randomString.nextAlphabetic(titleRange.random())
        val maxTitle = randomString.nextAlphabetic(MAX_TITLE_LENGTH)

        listOf(minTitle, title, maxTitle).forEach { Title.valueOf(it) }
    }

    @Test
    fun `invalid title length`() {
        val title = randomString.nextAlphabetic(MAX_TITLE_LENGTH + 1)
        val exception = assertThrows<ModelValidationException> { Title.valueOf(title) }
        assertContains(exception.message!!, "invalid title", true)
    }

    @Test
    fun `invalid blank title`() {
        val emptyTitle = ""
        val blankTitle = " "
        listOf(emptyTitle, blankTitle).forEach {
            val exception = assertThrows<ModelValidationException> { Title.valueOf(it) }
            assertContains(exception.message!!, "invalid title", true)
        }
    }
}