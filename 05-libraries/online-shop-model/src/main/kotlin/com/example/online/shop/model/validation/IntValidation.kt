package com.example.online.shop.model.validation

internal fun Int.requireRange(min: Int, max: Int, exception: () -> ModelValidationException): Int {
    if (this < min || this > max) {
        throw exception()
    }
    return this
}