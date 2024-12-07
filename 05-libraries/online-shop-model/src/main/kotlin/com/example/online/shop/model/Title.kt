package com.example.online.shop.model

import com.example.online.shop.model.TitleUtils.validate
import com.example.online.shop.model.validation.ModelValidationException
import com.example.online.shop.model.validation.rejectFormat
import com.example.online.shop.model.validation.requireNotEmpty
import com.example.online.shop.model.validation.requireRange
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

@JvmInline
value class Title(private val value: String) : Model {

    init {
        value.validate()
    }

    companion object {
        @JvmStatic
        @JsonCreator
        fun valueOf(value: String): Title = Title(value)
    }

    @JsonValue
    override fun toString(): String = value
}

private object TitleUtils {
    private const val MIN_LENGTH = 1
    private const val MAX_LENGTH = 150
    private val xssScriptRegex = Regex("""/(\b)(on\w+)=|javascript|(<\s*)(/*)script/gi""")

    fun String.validate(): String = this
        .requireNotEmpty {
            throw ModelValidationException("Invalid title: $this; Title is empty")
        }
        .requireRange(MIN_LENGTH, MAX_LENGTH) {
            throw ModelValidationException("Invalid title: $this; Length is $length, but must be within $MIN_LENGTH and $MAX_LENGTH")
        }.rejectFormat(xssScriptRegex) {
            throw ModelValidationException("Invalid title: $this; Title must not contain scripts")
        }
}