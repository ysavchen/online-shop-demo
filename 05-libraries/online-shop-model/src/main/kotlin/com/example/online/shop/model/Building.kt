package com.example.online.shop.model

import com.example.online.shop.model.BuildingUtils.formatValue
import com.example.online.shop.model.BuildingUtils.validate
import com.example.online.shop.model.validation.*
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

@JvmInline
value class Building(private val value: String) : Model<String> {

    init {
        value.validate()
    }

    companion object {
        @JvmStatic
        @JsonCreator
        fun valueOf(value: String): Building = Building(value.validate().formatValue())
    }

    @get:JsonValue
    override val formattedValue: String
        get() = value.formatValue()

    override fun toString(): String = formattedValue
}

private object BuildingUtils {
    private const val MIN_LENGTH = 1
    private const val MAX_LENGTH = 10

    fun String.validate(): String = this
        .requireNotBlank {
            throw ModelValidationException("Invalid building: $this; Building is blank")
        }
        .requireRange(MIN_LENGTH, MAX_LENGTH) {
            throw ModelValidationException("Invalid building: $this; Length is $length, but must be within $MIN_LENGTH and $MAX_LENGTH")
        }
        .rejectFormats(xssPatterns) {
            throw ModelValidationException("Invalid building: $this; Building must not contain scripts")
        }

    fun String.formatValue(): String = this.trim()
}