package com.example.online.shop.model

import com.example.online.shop.model.PriceValueUtils.formatValue
import com.example.online.shop.model.PriceValueUtils.validate
import com.example.online.shop.model.validation.ModelValidationException
import com.example.online.shop.model.validation.requireRange
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

@JvmInline
value class PriceValue(private val value: BigDecimal) : Model<BigDecimal> {

    init {
        value.validate()
    }

    companion object {
        @JvmStatic
        @JsonCreator
        fun valueOf(value: BigDecimal): PriceValue = PriceValue(value.validate().formatValue())
    }

    @get:JsonValue
    override val formattedValue: BigDecimal
        get() = value.formatValue()

    override fun toString(): String = formattedValue.toString()
}

internal object PriceValueUtils {
    val MIN_PRICE_VALUE: BigDecimal = BigDecimal.valueOf(0.0)
    val MAX_PRICE_VALUE: BigDecimal = BigDecimal.valueOf(9999999.99)

    fun BigDecimal.validate(): BigDecimal = this
        .requireRange(MIN_PRICE_VALUE, MAX_PRICE_VALUE) {
            throw ModelValidationException("Invalid priceValue: $this; PriceValue is $this, but must be within $MIN_PRICE_VALUE and $MAX_PRICE_VALUE")
        }

    fun BigDecimal.formatValue(): BigDecimal = this.round(MathContext(9, RoundingMode.HALF_EVEN))
}