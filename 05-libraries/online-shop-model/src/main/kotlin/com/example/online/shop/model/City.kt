package com.example.online.shop.model

import com.example.online.shop.model.CityUtils.formatValue
import com.example.online.shop.model.CityUtils.validate
import com.example.online.shop.model.validation.ModelValidationException
import com.example.online.shop.model.validation.requireNotBlank
import com.example.online.shop.model.validation.requireRange
import com.example.online.shop.model.validation.sanitize
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

@JvmInline
value class City(private val value: String) : Model<String> {

    init {
        value.validate()
    }

    companion object {
        @JvmStatic
        @JsonCreator
        fun valueOf(value: String): City = City(value.validate().formatValue())
    }

    @get:JsonValue
    override val formattedValue: String
        get() = value.formatValue()

    override fun toString(): String = formattedValue
}

internal object CityUtils {
    const val MIN_CITY_LENGTH = 1
    const val MAX_CITY_LENGTH = 100

    fun String.validate(): String = this
        .requireNotBlank {
            throw ModelValidationException("Invalid city: $this; City is blank")
        }
        .requireRange(MIN_CITY_LENGTH, MAX_CITY_LENGTH) {
            throw ModelValidationException("Invalid city: $this; Length is $length, but must be within $MIN_CITY_LENGTH and $MAX_CITY_LENGTH")
        }
        .sanitize()

    fun String.formatValue(): String = this.trim()
}