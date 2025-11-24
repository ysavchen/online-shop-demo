package com.example.online.shop.model

import com.example.online.shop.model.DescriptionUtils.formatValue
import com.example.online.shop.model.DescriptionUtils.validate
import com.example.online.shop.model.validation.ModelValidationException
import com.example.online.shop.model.validation.rejectFormat
import com.example.online.shop.model.validation.requireNotBlank
import com.example.online.shop.model.validation.xssScriptRegex
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

private object DescriptionUtils {

    fun String.validate(): String = this
        .requireNotBlank {
            throw ModelValidationException("Invalid description: $this; Description is blank")
        }
        .rejectFormat(xssScriptRegex) {
            throw ModelValidationException("Invalid description: $this; Description must not contain scripts")
        }

    fun String.formatValue(): String = this.trim()
}