package com.example.online.shop.model

import com.example.online.shop.model.validation.IsbnValidationException
import com.example.online.shop.model.validation.requireFormat
import com.example.online.shop.model.validation.requireRange

@JvmInline
value class Isbn(private val rawValue: String) {

    companion object {
        private const val MIN_LENGTH = 13
        private const val MAX_LENGTH = 17
        private val isbnRegex = Regex(
            """
                ^(?:ISBN(?:-1[03])?:? )?
                (?=[0-9X]{10}${'$'}|
                (?=(?:[0-9]+[- ]){3})[- 0-9X]{13}${'$'}|
                97[89][0-9]{10}${'$'}|
                (?=(?:[0-9]+[- ]){4})[- 0-9]{17}${'$'})
                (?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]${'$'}
            """.trimIndent()
        )

        fun validate(value: String): String = value
            .requireRange(MIN_LENGTH, MAX_LENGTH) {
                throw IsbnValidationException("Invalid range: $value. Range must be within $MIN_LENGTH and $MAX_LENGTH")
            }
            .requireFormat(isbnRegex) { throw IsbnValidationException("Invalid ISBN: $value") }
    }
}