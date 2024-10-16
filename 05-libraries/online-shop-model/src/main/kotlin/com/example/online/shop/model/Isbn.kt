package com.example.online.shop.model

import com.example.online.shop.model.validation.IsbnValidationException
import com.example.online.shop.model.validation.requireFormat
import com.example.online.shop.model.validation.requireRange

@JvmInline
value class Isbn(private val rawValue: String) : Model {

    init {
        validate(rawValue)
    }

    companion object {
        private const val MIN_LENGTH = 13
        private const val MAX_LENGTH = 25
        private val isbnRegex = Regex(
            """
               ^(?:ISBN(?:-13)?:? )?(?=[0-9]{13}${'$'}|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}${'$'})97[89][- ]?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9]${'$'}
            """.trimIndent()
        )

        fun validate(value: String): String = value
            .requireRange(MIN_LENGTH, MAX_LENGTH) {
                throw IsbnValidationException("Invalid ISBN: $value. Length is ${value.length}, but must be within $MIN_LENGTH and $MAX_LENGTH")
            }
            .requireFormat(isbnRegex) { throw IsbnValidationException("Invalid ISBN: $value") }

        private fun format(value: String): String = TODO()
    }

    /**
     * ISBN 978-1-42051-505-3
     */
    override val formattedValue: String
        get() = format(rawValue)
}