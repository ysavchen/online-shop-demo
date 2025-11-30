package com.example.online.shop.model

import com.example.online.shop.model.DescriptionUtils.formatValue
import com.example.online.shop.model.DescriptionUtils.validate
import com.example.online.shop.model.validation.ModelValidationException
import com.example.online.shop.model.validation.requireNotBlank
import com.example.online.shop.model.validation.requireRange
import com.example.online.shop.model.validation.sanitize
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

@JvmInline
value class Description(private val value: String) : Model<String> {

    init {
        value.validate()
    }

    companion object {
        @JvmStatic
        @JsonCreator
        fun valueOf(value: String): Description = Description(value.validate().formatValue())
    }

    @get:JsonValue
    override val formattedValue: String
        get() = value.formatValue()

    override fun toString(): String = formattedValue
}

internal object DescriptionUtils {
    const val MIN_DESCRIPTION_LENGTH = 1
    const val MAX_DESCRIPTION_LENGTH = 5000

    fun String.validate(): String = this
        .requireNotBlank {
            throw ModelValidationException("Invalid description: $this; Description is blank")
        }
        .requireRange(MIN_DESCRIPTION_LENGTH, MAX_DESCRIPTION_LENGTH) {
            throw ModelValidationException("Invalid description: $this; Length is $length, but must be within $MIN_DESCRIPTION_LENGTH and $MAX_DESCRIPTION_LENGTH")
        }
        .sanitize()

    fun String.formatValue(): String = this.trim()
}