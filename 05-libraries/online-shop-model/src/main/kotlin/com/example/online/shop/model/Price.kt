package com.example.online.shop.model

import com.example.online.shop.model.PriceUtils.formatValue
import com.example.online.shop.model.PriceUtils.validate
import com.example.online.shop.model.validation.ModelValidationException
import com.example.online.shop.model.validation.requireRange
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

@JvmInline
value class Price(private val value: BigDecimal) : Model<BigDecimal> {

    init {
        value.validate()
    }

    companion object {
        @JvmStatic
        @JsonCreator
        fun valueOf(value: BigDecimal): Price = Price(value.validate().formatValue())
    }

    @get:JsonValue
    override val formattedValue: BigDecimal
        get() = value.formatValue()

    override fun toString(): String = formattedValue.toString()
}

private object PriceUtils {
    private val MIN_PRICE = BigDecimal.valueOf(0.0)
    private val MAX_PRICE = BigDecimal.valueOf(9999999.99)

    fun BigDecimal.validate(): BigDecimal = this
        .requireRange(MIN_PRICE, MAX_PRICE) {
            throw ModelValidationException("Invalid price: $this; Price is $this, but must be within $MIN_PRICE and $MAX_PRICE")
        }

    fun BigDecimal.formatValue(): BigDecimal = this.round(MathContext(9, RoundingMode.HALF_EVEN))
}