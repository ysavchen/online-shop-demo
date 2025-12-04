package com.example.online.shop.model

import com.example.online.shop.model.StreetUtils.formatValue
import com.example.online.shop.model.StreetUtils.validate
import com.example.online.shop.model.validation.ModelValidationException
import com.example.online.shop.model.validation.requireNotBlank
import com.example.online.shop.model.validation.requireRange
import com.example.online.shop.model.validation.sanitize
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

@JvmInline
value class Street(private val rawValue: String) : Model<String> {

    init {
        rawValue.validate()
    }

    companion object {
        @JvmStatic
        @JsonCreator
        fun valueOf(rawValue: String): Street = Street(rawValue)
    }

    @get:JsonValue
    override val value: String
        get() = rawValue.formatValue()

    override fun toString(): String = value
}

internal object StreetUtils {
    const val MIN_STREET_LENGTH = 1
    const val MAX_STREET_LENGTH = 150

    fun String.validate(): String = this
        .requireNotBlank {
            throw ModelValidationException("Invalid street: $this; Street is blank")
        }
        .requireRange(MIN_STREET_LENGTH, MAX_STREET_LENGTH) {
            throw ModelValidationException("Invalid street: $this; Length is $length, but must be within $MIN_STREET_LENGTH and $MAX_STREET_LENGTH")
        }
        .sanitize()

    fun String.formatValue(): String = this.trim()
}