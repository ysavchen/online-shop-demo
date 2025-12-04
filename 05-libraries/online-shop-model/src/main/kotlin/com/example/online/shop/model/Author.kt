package com.example.online.shop.model

import com.example.online.shop.model.Author.Companion.valueOf
import com.example.online.shop.model.AuthorUtils.formatValue
import com.example.online.shop.model.AuthorUtils.validate
import com.example.online.shop.model.validation.ModelValidationException
import com.example.online.shop.model.validation.requireNotBlank
import com.example.online.shop.model.validation.requireRange
import com.example.online.shop.model.validation.sanitize
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

@JvmInline
value class Author(private val rawValue: String) : Model<String> {

    init {
        rawValue.validate()
    }

    /**
     * При использовании [valueOf] происходит избыточная валидация [rawValue]: сначала в самом [valueOf], потом в `init` блоке.
     * Почему нельзя убрать `validate()` в [valueOf] или в `init` блоке, чтобы исключить дублирование валидации?
     *
     * 1. [valueOf] используется для парсинга json в REST API.
     * Но создавать объект через конструктор иногда тоже удобно, например, в тестах или маппинге `entity` в `model`.
     * Поэтому поддерживаются оба способа создания объекта.
     *
     * 2. Оставить только `rawValue.formatValue()` в [valueOf] тоже нельзя, т.к. имеет смысл сначала валидировать, а потом форматировать.
     * Если сначала форматировать невалидные данные, то мы можем получить ошибку форматирования, а не валидации.
     * Т.е. когда мы получаем невалидные данные, нам нужно, чтобы сервис отдавал ошибку валидации.
     *
     * 3. Удобно, когда данные сохраняются в базу данных в едином формате, поэтому в [valueOf] данные форматируются.
     *
     * 4. В редких случаях дополнительная валидация после форматирования помогает отловить ошибки форматирования.
     */
    companion object {
        @JvmStatic
        @JsonCreator
        fun valueOf(rawValue: String): Author = Author(rawValue.validate().formatValue())
    }

    @get:JsonValue
    override val value: String
        get() = rawValue.formatValue()

    override fun toString(): String = value
}

internal object AuthorUtils {
    const val MIN_AUTHOR_LENGTH = 1
    const val MAX_AUTHOR_LENGTH = 70

    fun String.validate(): String = this
        .requireNotBlank {
            throw ModelValidationException("Invalid author: $this; Author is blank")
        }
        .requireRange(MIN_AUTHOR_LENGTH, MAX_AUTHOR_LENGTH) {
            throw ModelValidationException("Invalid author: $this; Length is $length, but must be within $MIN_AUTHOR_LENGTH and $MAX_AUTHOR_LENGTH")
        }
        .sanitize()

    fun String.formatValue(): String = this.trim()
}