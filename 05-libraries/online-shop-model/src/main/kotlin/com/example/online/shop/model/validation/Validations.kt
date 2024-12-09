package com.example.online.shop.model.validation

internal val xssScriptRegex = Regex("""/(\b)(on\w+)=|javascript|(<\s*)(/*)script/gi""")

internal fun String.requireNotEmpty(exception: () -> ModelValidationException): String {
    if (this.isEmpty()) {
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

internal fun String.rejectFormat(regex: Regex, exception: () -> ModelValidationException): String {
    if (this.matches(regex)) {
        throw exception()
    }
    return this
}