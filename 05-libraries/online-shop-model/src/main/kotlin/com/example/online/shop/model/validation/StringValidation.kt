package com.example.online.shop.model.validation

import org.owasp.html.PolicyFactory
import org.owasp.html.Sanitizers

private val policy: PolicyFactory = Sanitizers.FORMATTING.and(Sanitizers.LINKS)

internal fun String.requireNotBlank(exception: () -> ModelValidationException): String {
    if (this.isBlank()) {
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

internal fun String.sanitize(): String = policy.sanitize(this)
