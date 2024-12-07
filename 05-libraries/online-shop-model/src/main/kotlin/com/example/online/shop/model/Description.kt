package com.example.online.shop.model

import com.example.online.shop.model.DescriptionUtils.validate
import com.example.online.shop.model.validation.ModelValidationException
import com.example.online.shop.model.validation.rejectFormat
import com.example.online.shop.model.validation.requireNotEmpty
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

@JvmInline
value class Description(private val value: String) : Model {

    init {
        value.validate()
    }

    companion object {
        @JvmStatic
        @JsonCreator
        fun valueOf(value: String): Description = Description(value)
    }

    @get:JsonValue
    override val formattedValue: String
        get() = value

    override fun toString(): String = formattedValue
}

private object DescriptionUtils {
    private val xssScriptRegex = Regex("""/(\b)(on\w+)=|javascript|(<\s*)(/*)script/gi""")

    fun String.validate(): String = this
        .requireNotEmpty {
            throw ModelValidationException("Invalid description: $this; Description is empty")
        }
        .rejectFormat(xssScriptRegex) {
            throw ModelValidationException("Invalid description: $this; Description must not contain scripts")
        }
}