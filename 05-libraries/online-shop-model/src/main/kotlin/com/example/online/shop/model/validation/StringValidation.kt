package com.example.online.shop.model.validation

internal val xssPatterns = listOf(
    Regex("""/<script\b[^>]*>.*?<\/script\s*>/gi"""),                    // script tags
    Regex("""/on\w+\s*=\s*["']?[^"']*["']?/gi"""),                       // event handlers
    Regex("""/javascript:\s*[^"'\s<>]*/gi"""),                           // javascript protocol
    Regex("""/<\s*(?:iframe|form|meta|object|embed|applet)[^>]*>/gi"""), // dangerous tags
    Regex("""/<\s*(?:style|link)[^>]*>/gi"""),                           // style and link
    Regex("""/(?:eval|alert|prompt|confirm)\s*\([^)]*\)/gi"""),          // dangerous functions
    Regex("""/<\s*img[^>]*\s+onerror\s*=[^>]*>/gi"""),                   // img on error
    Regex("""/\b(?:expression|url)\s*\([^)]*\)/gi""")                    // expression
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