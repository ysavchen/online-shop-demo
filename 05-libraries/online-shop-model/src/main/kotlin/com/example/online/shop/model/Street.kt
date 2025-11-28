package com.example.online.shop.model

import com.fasterxml.jackson.annotation.JsonValue

@JvmInline
value class Street(private val value: String) : Model<String> {

    @get:JsonValue
    override val formattedValue: String
        get() = value

}