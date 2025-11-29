package com.example.online.shop.model

import com.example.online.shop.model.StreetUtils.formatValue
import com.example.online.shop.model.StreetUtils.validate
import com.example.online.shop.model.validation.*
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

@JvmInline
value class Street(private val value: String) : Model<String> {

    init {
        value.validate()
    }

    companion object {
        @JvmStatic
        @JsonCreator
        fun valueOf(value: String): Street = Street(value.validate().formatValue())
    }

    @get:JsonValue
    override val formattedValue: String
        get() = value.formatValue()

    override fun toString(): String = formattedValue
}

private object StreetUtils {
    private const val MIN_LENGTH = 1
    private const val MAX_LENGTH = 150

    fun String.validate(): String = this
        .requireNotBlank {
            throw ModelValidationException("Invalid street: $this; Street is blank")
        }
        .requireRange(MIN_LENGTH, MAX_LENGTH) {
            throw ModelValidationException("Invalid street: $this; Length is $length, but must be within $MIN_LENGTH and $MAX_LENGTH")
        }
        .rejectFormats(xssPatterns) {
            throw ModelValidationException("Invalid street: $this; Street must not contain scripts")
        }

    fun String.formatValue(): String = this.trim()
}