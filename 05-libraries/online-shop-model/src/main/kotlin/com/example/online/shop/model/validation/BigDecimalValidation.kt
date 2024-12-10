package com.example.online.shop.model.validation

import java.math.BigDecimal

internal fun BigDecimal.requireRange(
    min: BigDecimal,
    max: BigDecimal,
    exception: () -> ModelValidationException
): BigDecimal {
    if (this < min || this > max) {
        throw exception()
    }
    return this
}