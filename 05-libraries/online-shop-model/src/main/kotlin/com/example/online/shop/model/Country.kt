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
value class Country(private val rawValue: String) : Model<String> {

    init {
        rawValue.validate()
    }

    companion object {
        @JvmStatic
        @JsonCreator
        fun valueOf(rawValue: String): Country = Country(rawValue)
    }

    @get:JsonValue
    override val value: String
        get() = rawValue.formatValue()

    override fun toString(): String = value
}

internal object CountryUtils {
    const val MIN_COUNTRY_LENGTH = 1
    const val MAX_COUNTRY_LENGTH = 100

    fun String.validate(): String = this
        .requireNotBlank {
            throw ModelValidationException("Invalid country: $this; Country is blank")
        }
        .requireRange(MIN_COUNTRY_LENGTH, MAX_COUNTRY_LENGTH) {
            throw ModelValidationException("Invalid country: $this; Length is $length, but must be within $MIN_COUNTRY_LENGTH and $MAX_COUNTRY_LENGTH")
        }
        .sanitize()

    fun String.formatValue(): String = this.trim()
}