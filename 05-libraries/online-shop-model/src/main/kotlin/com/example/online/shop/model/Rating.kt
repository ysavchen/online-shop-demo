package com.example.online.shop.model

import com.example.online.shop.model.RatingUtils.formatValue
import com.example.online.shop.model.RatingUtils.validate
import com.example.online.shop.model.validation.ModelValidationException
import com.example.online.shop.model.validation.requireRange
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

@JvmInline
value class Rating(private val rawValue: BigDecimal) : Model<BigDecimal> {

    init {
        rawValue.validate()
    }

    companion object {
        @JvmStatic
        @JsonCreator
        fun valueOf(rawValue: BigDecimal): Rating = Rating(rawValue)
    }

    @get:JsonValue
    override val value: BigDecimal
        get() = rawValue.formatValue()

    override fun toString(): String = value.toString()
}

internal object RatingUtils {
    val MIN_RATING: BigDecimal = BigDecimal.valueOf(1.0)
    val MAX_RATING: BigDecimal = BigDecimal.valueOf(5.0)

    fun BigDecimal.validate(): BigDecimal = this
        .requireRange(MIN_RATING, MAX_RATING) {
            throw ModelValidationException("Invalid rating: $this; Rating is $this, but must be within $MIN_RATING and $MAX_RATING")
        }

    fun BigDecimal.formatValue(): BigDecimal = this.round(MathContext(2, RoundingMode.HALF_EVEN))
}