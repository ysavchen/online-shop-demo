package com.example.bookservice.test

import tools.jackson.core.type.TypeReference
import java.math.BigDecimal
import java.math.MathContext
import java.time.LocalDate
import java.util.concurrent.ThreadLocalRandom
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

inline fun <reified T> jacksonTypeRef(): TypeReference<T> = object : TypeReference<T>() {}


fun randomLocalDate(): LocalDate {
    val hundredYears = 100 * 365
    return LocalDate.ofEpochDay(
        ThreadLocalRandom.current().nextInt(-hundredYears, hundredYears).toLong()
    )
}

fun randomPrice(): BigDecimal =
    BigDecimal(Random.nextDouble(from = 100.00, until = 999.99)).round(MathContext(5))

fun randomRating(): BigDecimal =
    BigDecimal(Random.nextDouble(from = 1.0, until = 5.0)).round(MathContext(2))