package com.example.online.shop.model

import com.example.online.shop.model.ReviewTextUtils.formatValue
import com.example.online.shop.model.ReviewTextUtils.validate
import com.example.online.shop.model.validation.*
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

@JvmInline
value class ReviewText(private val value: String) : Model<String> {

    init {
        value.validate()
    }

    companion object {
        @JvmStatic
        @JsonCreator
        fun valueOf(value: String): ReviewText = ReviewText(value.validate().formatValue())
    }

    @get:JsonValue
    override val formattedValue: String
        get() = value.formatValue()

    override fun toString(): String = formattedValue
}

private object ReviewTextUtils {
    private const val MIN_LENGTH = 1
    private const val MAX_LENGTH = 5000

    fun String.validate(): String = this
        .requireNotBlank {
            throw ModelValidationException("Invalid reviewText: $this; ReviewText is blank")
        }
        .requireRange(MIN_LENGTH, MAX_LENGTH) {
            throw ModelValidationException("Invalid reviewText: $this; Length is $length, but must be within $MIN_LENGTH and $MAX_LENGTH")
        }
        .rejectFormats(xssPatterns) {
            throw ModelValidationException("Invalid reviewText: $this; ReviewText must not contain scripts")
        }

    fun String.formatValue(): String = this.trim()
}