package com.example.online.shop.model

import com.example.online.shop.model.CountryUtils.formatValue
import com.example.online.shop.model.CountryUtils.validate
import com.example.online.shop.model.validation.ModelValidationException
import com.example.online.shop.model.validation.requireNotBlank
import com.example.online.shop.model.validation.requireRange
import com.example.online.shop.model.validation.sanitize
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

@JvmInline
value class Country(private val value: String) : Model<String> {

    init {
        value.validate()
    }

    companion object {
        @JvmStatic
        @JsonCreator
        fun valueOf(value: String): Country = Country(value.validate().formatValue())
    }

    @get:JsonValue
    override val formattedValue: String
        get() = value.formatValue()

    override fun toString(): String = formattedValue
}

internal object CountryUtils {
    const val MIN_LENGTH = 1
    const val MAX_LENGTH = 100

    fun String.validate(): String = this
        .requireNotBlank {
            throw ModelValidationException("Invalid country: $this; Country is blank")
        }
        .requireRange(MIN_LENGTH, MAX_LENGTH) {
            throw ModelValidationException("Invalid country: $this; Length is $length, but must be within $MIN_LENGTH and $MAX_LENGTH")
        }
        .sanitize()

    fun String.formatValue(): String = this.trim()
}