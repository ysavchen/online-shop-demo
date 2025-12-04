package com.example.online.shop.model

import com.example.online.shop.model.ReviewTextUtils.formatValue
import com.example.online.shop.model.ReviewTextUtils.validate
import com.example.online.shop.model.validation.ModelValidationException
import com.example.online.shop.model.validation.requireNotBlank
import com.example.online.shop.model.validation.requireRange
import com.example.online.shop.model.validation.sanitize
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

@JvmInline
value class ReviewText(private val rawValue: String) : Model<String> {

    init {
        rawValue.validate()
    }

    companion object {
        @JvmStatic
        @JsonCreator
        fun valueOf(rawValue: String): ReviewText = ReviewText(rawValue.validate().formatValue())
    }

    @get:JsonValue
    override val value: String
        get() = rawValue.formatValue()

    override fun toString(): String = value
}

internal object ReviewTextUtils {
    const val MIN_REVIEW_TEXT_LENGTH = 1
    const val MAX_REVIEW_TEXT_LENGTH = 5000

    fun String.validate(): String = this
        .requireNotBlank {
            throw ModelValidationException("Invalid reviewText: $this; ReviewText is blank")
        }
        .requireRange(MIN_REVIEW_TEXT_LENGTH, MAX_REVIEW_TEXT_LENGTH) {
            throw ModelValidationException("Invalid reviewText: $this; Length is $length, but must be within $MIN_REVIEW_TEXT_LENGTH and $MAX_REVIEW_TEXT_LENGTH")
        }
        .sanitize()

    fun String.formatValue(): String = this.trim()
}