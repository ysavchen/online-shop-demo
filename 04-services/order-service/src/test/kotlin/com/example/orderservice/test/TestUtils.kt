package com.example.orderservice.test

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

fun randomPrice(): BigDecimal =
    BigDecimal(Random.nextDouble(from = 100.00, until = 999.99)).round(MathContext(5))

fun randomLocalDate(): LocalDate {
    val hundredYears = 100 * 365
    return LocalDate.ofEpochDay(
        ThreadLocalRandom.current().nextInt(-hundredYears, hundredYears).toLong()
    )
}