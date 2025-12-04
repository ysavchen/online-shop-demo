package com.example.online.shop.model

import com.example.online.shop.model.BuildingUtils.formatValue
import com.example.online.shop.model.BuildingUtils.validate
import com.example.online.shop.model.validation.ModelValidationException
import com.example.online.shop.model.validation.requireNotBlank
import com.example.online.shop.model.validation.requireRange
import com.example.online.shop.model.validation.sanitize
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

@JvmInline
value class Building(private val rawValue: String) : Model<String> {

    init {
        rawValue.validate()
    }

    companion object {
        @JvmStatic
        @JsonCreator
        fun valueOf(rawValue: String): Building = Building(rawValue)
    }

    @get:JsonValue
    override val value: String
        get() = rawValue.formatValue()

    override fun toString(): String = value
}

internal object BuildingUtils {
    const val MIN_BUILDING_LENGTH = 1
    const val MAX_BUILDING_LENGTH = 10

    fun String.validate(): String = this
        .requireNotBlank {
            throw ModelValidationException("Invalid building: $this; Building is blank")
        }
        .requireRange(MIN_BUILDING_LENGTH, MAX_BUILDING_LENGTH) {
            throw ModelValidationException("Invalid building: $this; Length is $length, but must be within $MIN_BUILDING_LENGTH and $MAX_BUILDING_LENGTH")
        }
        .sanitize()

    fun String.formatValue(): String = this.trim()
}