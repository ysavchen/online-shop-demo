package com.example.online.shop.model

import com.fasterxml.jackson.annotation.JsonValue
import java.math.BigDecimal

@JvmInline
value class Rating(private val value: BigDecimal) : Model<BigDecimal> {

    @get:JsonValue
    override val formattedValue: BigDecimal
        get() = value

}