package com.example.online.shop.model.validation

internal fun String.requireSize(size: Int, exception: () -> ModelValidationException): String {
    if (length != size) {
        throw exception()
    }
    return this
}

internal fun String.requireRange(min: Int, max: Int, exception: () -> ModelValidationException): String {
    if (length < min || length > max) {
        throw exception()
    }
    return this
}

internal fun String.requireFormat(regex: Regex, exception: () -> ModelValidationException): String {
    if (!this.matches(regex)) {
        throw exception()
    }
    return this
}