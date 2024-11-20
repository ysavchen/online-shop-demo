package com.example.online.shop.model

import com.example.online.shop.model.validation.IsbnValidationException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertContains
import kotlin.test.assertEquals

class IsbnTests {

    @Test
    fun `valid ISBN formats`() {
        val numbers = listOf(
            "978-1-42051-505-3",
            "9781420515053",
            "978 1 42051 505 3",
            "ISBN 978-1-42051-505-3",
            "ISBN 9781420515053",
            "ISBN 978 1 42051 505 3",
            "ISBN-13 978-1-42051-505-3",
            "ISBN-13 9781420515053",
            "ISBN-13 978 1 42051 505 3"
        )

        numbers.forEach { Isbn.valueOf(it) }
    }

    @Test
    fun `invalid ISBN length`() {
        val numbers = listOf(
            "978142051505",
            "ISBN-13 978-1-42051-505-34"
        )

        numbers.forEach {
            val exception = assertThrows<IsbnValidationException> { Isbn.valueOf(it) }
            assertContains(exception.message!!, "length", true)
        }
    }

    @Test
    fun `invalid ISBN formats`() {
        val numbers = listOf(
            "978_1_42051_505_3",
            "ISBN 978_1_42051_505_3",
            "ISBN-13 978_1_42051_505_3"
        )

        numbers.forEach {
            val exception = assertThrows<IsbnValidationException> { Isbn.valueOf(it) }
            assertContains(exception.message!!, "format", true)
        }
    }

    @Test
    fun `format ISBN`() {
        val numbers = listOf(
            "978-1-42051-505-3",
            "9781420515053",
            "978 1 42051 505 3",
            "ISBN 978-1-42051-505-3",
            "ISBN 9781420515053",
            "ISBN 978 1 42051 505 3",
            "ISBN-13 978-1-42051-505-3",
            "ISBN-13 9781420515053",
            "ISBN-13 978 1 42051 505 3"
        )
        val expectedValue = "978-1-42051-505-3"

        numbers.forEach {
            assertEquals(expectedValue, Isbn.valueOf(it).toString())
        }
    }
}