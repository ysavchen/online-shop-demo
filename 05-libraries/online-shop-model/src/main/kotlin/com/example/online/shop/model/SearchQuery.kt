package com.example.online.shop.model

import com.example.online.shop.model.SearchQueryUtils.formatValue
import com.example.online.shop.model.SearchQueryUtils.validate
import com.example.online.shop.model.validation.ModelValidationException
import com.example.online.shop.model.validation.requireRange
import com.example.online.shop.model.validation.sanitize
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

@JvmInline
value class SearchQuery(private val rawValue: String) : Model<String> {

    init {
        rawValue.validate()
    }

    companion object {
        @JvmStatic
        @JsonCreator
        fun valueOf(rawValue: String): SearchQuery = SearchQuery(rawValue.validate().formatValue())
    }

    @get:JsonValue
    override val value: String
        get() = rawValue.formatValue()

    override fun toString(): String = value
}

internal object SearchQueryUtils {
    const val MIN_SEARCH_QUERY_LENGTH = 0
    const val MAX_SEARCH_QUERY_LENGTH = 300

    fun String.validate(): String = this
        .requireRange(MIN_SEARCH_QUERY_LENGTH, MAX_SEARCH_QUERY_LENGTH) {
            throw ModelValidationException("Invalid searchQuery: $this; Length is $length, but must be within $MIN_SEARCH_QUERY_LENGTH and $MAX_SEARCH_QUERY_LENGTH")
        }
        .sanitize()

    fun String.formatValue(): String = this.trim()
}