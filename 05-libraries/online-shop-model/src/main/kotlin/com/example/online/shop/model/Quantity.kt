package com.example.online.shop.model

import com.example.online.shop.model.QuantityUtils.validate
import com.example.online.shop.model.validation.ModelValidationException
import com.example.online.shop.model.validation.requireRange
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

@JvmInline
value class Quantity(private val value: Int) : Model<Int> {

    init {
        value.validate()
    }

    companion object {
        @JvmStatic
        @JsonCreator
        fun valueOf(value: Int): Quantity = Quantity(value)
    }

    operator fun plus(increment: Quantity): Quantity = valueOf(value + increment.formattedValue)

    operator fun minus(decrement: Quantity): Quantity = valueOf(value - decrement.formattedValue)

    @get:JsonValue
    override val formattedValue: Int
        get() = value

    override fun toString(): String = formattedValue.toString()
}

private object QuantityUtils {
    private const val MIN_QUANTITY = 0
    private const val MAX_QUANTITY = Int.MAX_VALUE

    fun Int.validate(): Int = this
        .requireRange(MIN_QUANTITY, MAX_QUANTITY) {
            throw ModelValidationException("Invalid quantity: $this; Quantity is $this, but must be within $MIN_QUANTITY and $MAX_QUANTITY")
        }
}