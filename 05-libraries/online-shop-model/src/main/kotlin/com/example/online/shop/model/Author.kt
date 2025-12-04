package com.example.online.shop.model

import com.example.online.shop.model.AuthorUtils.formatValue
import com.example.online.shop.model.AuthorUtils.validate
import com.example.online.shop.model.validation.ModelValidationException
import com.example.online.shop.model.validation.requireNotBlank
import com.example.online.shop.model.validation.requireRange
import com.example.online.shop.model.validation.sanitize
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

@JvmInline
value class Author(private val rawValue: String) : Model<String> {

    init {
        rawValue.validate()
    }

    companion object {
        @JvmStatic
        @JsonCreator
        fun valueOf(rawValue: String): Author = Author(rawValue)
    }

    @get:JsonValue
    override val value: String
        get() = rawValue.formatValue()

    override fun toString(): String = value
}

internal object AuthorUtils {
    const val MIN_AUTHOR_LENGTH = 1
    const val MAX_AUTHOR_LENGTH = 70

    fun String.validate(): String = this
        .requireNotBlank {
            throw ModelValidationException("Invalid author: $this; Author is blank")
        }
        .requireRange(MIN_AUTHOR_LENGTH, MAX_AUTHOR_LENGTH) {
            throw ModelValidationException("Invalid author: $this; Length is $length, but must be within $MIN_AUTHOR_LENGTH and $MAX_AUTHOR_LENGTH")
        }
        .sanitize()

    fun String.formatValue(): String = this.trim()
}