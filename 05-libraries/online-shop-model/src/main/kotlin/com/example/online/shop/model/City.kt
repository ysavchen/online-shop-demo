package com.example.online.shop.model

import com.fasterxml.jackson.annotation.JsonValue

@JvmInline
value class City(private val value: String) : Model<String> {

    @get:JsonValue
    override val formattedValue: String
        get() = value

}