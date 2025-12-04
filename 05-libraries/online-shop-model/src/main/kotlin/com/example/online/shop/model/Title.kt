package com.example.online.shop.model

import com.example.online.shop.model.TitleUtils.formatValue
import com.example.online.shop.model.TitleUtils.validate
import com.example.online.shop.model.validation.ModelValidationException
import com.example.online.shop.model.validation.requireNotBlank
import com.example.online.shop.model.validation.requireRange
import com.example.online.shop.model.validation.sanitize
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

@JvmInline
value class Title(private val rawValue: String) : Model<String> {

    init {
        rawValue.validate()
    }

    companion object {
        @JvmStatic
        @JsonCreator
        fun valueOf(rawValue: String): Title = Title(rawValue)
    }

    @get:JsonValue
    override val value: String
        get() = rawValue.formatValue()

    override fun toString(): String = value
}

internal object TitleUtils {
    const val MIN_TITLE_LENGTH = 1
    const val MAX_TITLE_LENGTH = 150

    fun String.validate(): String = this
        .requireNotBlank {
            throw ModelValidationException("Invalid title: $this; Title is blank")
        }
        .requireRange(MIN_TITLE_LENGTH, MAX_TITLE_LENGTH) {
            throw ModelValidationException("Invalid title: $this; Length is $length, but must be within $MIN_TITLE_LENGTH and $MAX_TITLE_LENGTH")
        }
        .sanitize()

    fun String.formatValue(): String = this.trim()
}