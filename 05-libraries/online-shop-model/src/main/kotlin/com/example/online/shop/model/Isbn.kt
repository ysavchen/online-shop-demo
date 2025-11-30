package com.example.online.shop.model

import com.example.online.shop.model.IsbnUtils.formatValue
import com.example.online.shop.model.IsbnUtils.validate
import com.example.online.shop.model.validation.ModelValidationException
import com.example.online.shop.model.validation.requireFormat
import com.example.online.shop.model.validation.requireRange
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

@JvmInline
value class Isbn(private val value: String) : Model<String> {

    init {
        value.validate()
    }

    companion object {
        @JvmStatic
        @JsonCreator
        fun valueOf(value: String): Isbn = Isbn(value.validate().formatValue())
    }

    /**
     * 978-1-42051-505-3
     */
    @get:JsonValue
    override val formattedValue: String
        get() = value.formatValue()

    override fun toString(): String = formattedValue
}

internal object IsbnUtils {
    const val MIN_LENGTH = 13
    const val MAX_LENGTH = 25
    private val isbnRegex = Regex(
        """
           ^(?:ISBN(?:-13)?:? )?(?=[0-9]{13}${'$'}|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}${'$'})97[89][- ]?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9]${'$'}
        """.trimIndent()
    )

    fun String.validate(): String = this
        .requireRange(MIN_LENGTH, MAX_LENGTH) {
            throw ModelValidationException("Invalid ISBN: $this; Length is $length, but must be within $MIN_LENGTH and $MAX_LENGTH")
        }
        .requireFormat(isbnRegex) {
            throw ModelValidationException("Invalid ISBN: $this; Format must correspond to ISBN-13")
        }

    fun String.formatValue(): String {
        val onlyDigits = this
            .replace("ISBN-13", "")
            .replace("ISBN", "")
            .replace("-", "")
            .replace(" ", "")

        val eanPrefix = onlyDigits.substring(0, 3)
        val registrationGroup = onlyDigits.substring(3, 4)
        val registrant = onlyDigits.substring(4, 9)
        val publication = onlyDigits.substring(9, 12)
        val checkDigit = onlyDigits.last()

        return "$eanPrefix-$registrationGroup-$registrant-$publication-$checkDigit"
    }
}