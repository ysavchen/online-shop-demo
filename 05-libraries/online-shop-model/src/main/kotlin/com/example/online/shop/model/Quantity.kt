package com.example.online.shop.model

import com.example.online.shop.model.QuantityUtils.validate
import com.example.online.shop.model.validation.ModelValidationException
import com.example.online.shop.model.validation.requireRange
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

@JvmInline
value class Quantity(private val rawValue: Int) : Model<Int>, Comparable<Quantity> {

    init {
        rawValue.validate()
    }

    companion object {
        @JvmStatic
        @JsonCreator
        fun valueOf(rawValue: Int): Quantity = Quantity(rawValue)
    }

    operator fun plus(increment: Quantity): Quantity = valueOf(this.rawValue + increment.value)

    operator fun minus(decrement: Quantity): Quantity = valueOf(this.rawValue - decrement.value)

    override fun compareTo(other: Quantity): Int = this.rawValue.compareTo(other.value)

    @get:JsonValue
    override val value: Int
        get() = rawValue

    override fun toString(): String = value.toString()
}

internal object QuantityUtils {
    const val MIN_QUANTITY = 0
    const val MAX_QUANTITY = Int.MAX_VALUE

    fun Int.validate(): Int = this
        .requireRange(MIN_QUANTITY, MAX_QUANTITY) {
            throw ModelValidationException("Invalid quantity: $this; Quantity is $this, but must be within $MIN_QUANTITY and $MAX_QUANTITY")
        }
}