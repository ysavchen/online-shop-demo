package com.example.online.shop.model

import com.example.online.shop.model.TitleUtils.formatValue
import com.example.online.shop.model.TitleUtils.validate
import com.example.online.shop.model.validation.*
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
        fun valueOf(value: String): Title = Title(value.validate().formatValue())
    }

    @get:JsonValue
    override val formattedValue: String
        get() = value.formatValue()

    override fun toString(): String = formattedValue
}

private object TitleUtils {
    private const val MIN_LENGTH = 1
    private const val MAX_LENGTH = 150

    fun String.validate(): String = this
        .requireNotEmpty {
            throw ModelValidationException("Invalid title: $this; Title is empty")
        }
        .requireRange(MIN_LENGTH, MAX_LENGTH) {
            throw ModelValidationException("Invalid title: $this; Length is $length, but must be within $MIN_LENGTH and $MAX_LENGTH")
        }.rejectFormat(xssScriptRegex) {
            throw ModelValidationException("Invalid title: $this; Title must not contain scripts")
        }

    fun String.formatValue(): String = this.trim()
}