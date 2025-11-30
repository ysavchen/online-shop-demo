package com.example.online.shop.model

import com.example.online.shop.model.SearchQueryUtils.formatValue
import com.example.online.shop.model.SearchQueryUtils.validate
import com.example.online.shop.model.validation.ModelValidationException
import com.example.online.shop.model.validation.requireRange
import com.example.online.shop.model.validation.sanitize
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

@JvmInline
value class SearchQuery(private val value: String) : Model<String> {

    init {
        value.validate()
    }

    companion object {
        @JvmStatic
        @JsonCreator
        fun valueOf(value: String): SearchQuery = SearchQuery(value.validate().formatValue())
    }

    @get:JsonValue
    override val formattedValue: String
        get() = value.formatValue()

    override fun toString(): String = formattedValue
}

internal object SearchQueryUtils {
    const val MIN_LENGTH = 0
    const val MAX_LENGTH = 300

    fun String.validate(): String = this
        .requireRange(MIN_LENGTH, MAX_LENGTH) {
            throw ModelValidationException("Invalid searchQuery: $this; Length is $length, but must be within $MIN_LENGTH and $MAX_LENGTH")
        }
        .sanitize()

    fun String.formatValue(): String = this.trim()
}