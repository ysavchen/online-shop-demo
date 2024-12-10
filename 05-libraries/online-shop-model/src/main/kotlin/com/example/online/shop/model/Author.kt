package com.example.online.shop.model

import com.example.online.shop.model.AuthorUtils.formatValue
import com.example.online.shop.model.AuthorUtils.validate
import com.example.online.shop.model.validation.*
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

@JvmInline
value class Author(private val value: String) : Model<String> {

    init {
        value.validate()
    }

    companion object {
        @JvmStatic
        @JsonCreator
        fun valueOf(value: String): Author = Author(value.validate().formatValue())
    }

    @get:JsonValue
    override val formattedValue: String
        get() = value.formatValue()

    override fun toString(): String = formattedValue
}

private object AuthorUtils {
    private const val MIN_LENGTH = 1
    private const val MAX_LENGTH = 70

    fun String.validate(): String = this
        .requireNotEmpty {
            throw ModelValidationException("Invalid author: $this; Author is empty")
        }
        .requireRange(MIN_LENGTH, MAX_LENGTH) {
            throw ModelValidationException("Invalid author: $this; Length is $length, but must be within $MIN_LENGTH and $MAX_LENGTH")
        }
        .rejectFormat(xssScriptRegex) {
            throw ModelValidationException("Invalid author: $this; Author must not contain scripts")
        }

    fun String.formatValue(): String = this.trim()
}