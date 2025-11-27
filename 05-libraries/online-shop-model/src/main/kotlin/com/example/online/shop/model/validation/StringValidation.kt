package com.example.online.shop.model.validation

internal val xssPatterns = listOf(
    // script tags
    Regex("""<script\b[^>]*>(.*?)</script\s*>""", RegexOption.IGNORE_CASE),

    // event handlers
    Regex("""on\w+\s*=\s*["']?[^"']*["']?""", RegexOption.IGNORE_CASE),

    // javascript protocol
    Regex("""javascript:\s*[^"'\s<>]*""", RegexOption.IGNORE_CASE),

    // dangerous tags
    Regex("""<\s*(?:iframe|form|meta|object|embed|applet)[^>]*>""", RegexOption.IGNORE_CASE),

    // style and link
    Regex("""<\s*(?:style|link)[^>]*>""", RegexOption.IGNORE_CASE),

    // dangerous functions
    Regex("""(?:eval|alert|prompt|confirm)\s*\([^)]*\)""", RegexOption.IGNORE_CASE),

    // img on error
    Regex("""<\s*img[^>]*\s+onerror\s*=[^>]*>""", RegexOption.IGNORE_CASE),

    // expression
    Regex("""\b(?:expression|url)\s*\([^)]*\)""", RegexOption.IGNORE_CASE)
)

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

internal fun String.rejectFormat(regex: Regex, exception: () -> ModelValidationException): String {
    if (this.matches(regex)) {
        throw exception()
    }
    return this
}