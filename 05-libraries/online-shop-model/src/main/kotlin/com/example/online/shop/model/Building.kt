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