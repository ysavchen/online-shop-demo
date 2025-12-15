package com.example.service.support.test

import kotlin.random.Random

inline fun <reified T : Enum<T>> nextValue(predicate: (T) -> Boolean = { true }): T {
    val values = enumValues<T>().filter { predicate(it) }
    check(values.isNotEmpty())
    return if (values.size == 1) {
        values[0]
    } else {
        values[Random.nextInt(0, values.size - 1)]
    }
}