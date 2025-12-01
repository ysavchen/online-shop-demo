package com.example.online.shop.model.validation

internal fun Int.requireRange(min: Int, max: Int, exception: () -> ModelValidationException): Int {
    if (this !in min..max) {
        throw exception()
    }
    return this
}