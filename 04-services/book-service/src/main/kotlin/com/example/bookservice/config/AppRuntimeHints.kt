package com.example.bookservice.config

import liquibase.database.LiquibaseTableNamesFactory
import liquibase.parser.SqlParserFactory
import liquibase.report.ShowSummaryGeneratorFactory
import liquibase.ui.LoggerUIService
import org.springframework.aot.hint.ExecutableMode
import org.springframework.aot.hint.RuntimeHints
import org.springframework.aot.hint.RuntimeHintsRegistrar
import java.util.*

/**
 * GraalVM анализирует достижимость классов, полей и методов, чтобы потом удалить все лишнее.
 *
 * Хинты подсказывают, какие классы и методы понадобятся в Runtime,
 * чтобы GraalVM добавил их в native image.
 *
 * Обычно эти классы или методы вызываются динамически в рантайме через рефлексию или проксирование,
 * поэтому без хинта GraalVM не видит их во время компиляции.
 *
 * Например, класс может загружаться в JVM в рантайме после сканирования и создания Bean definitions.
 * И на момент статического анализа у GraalVM нет этой информации.
 *
 * Обычно о добавлении хинтов должны заботиться сами библиотеки, но пока еще не все отлажено.
 *
 * Также нужен хинт для своих кастомных ресурсов (напр. sql скриптов),
 * так как Spring Boot не добавит их автоматически.
 */
class AppRuntimeHints : RuntimeHintsRegistrar {
    override fun registerHints(hints: RuntimeHints, classLoader: ClassLoader?) {
        hints.resources().registerPattern("liquibase/*")
        hints.reflection()
            .registerType(LoggerUIService::class.java) { type ->
                type.withConstructor(Collections.emptyList(), ExecutableMode.INVOKE)
            }
            .registerType(LiquibaseTableNamesFactory::class.java) { type ->
                type.withConstructor(Collections.emptyList(), ExecutableMode.INVOKE)
            }
            .registerType(ShowSummaryGeneratorFactory::class.java) { type ->
                type.withConstructor(Collections.emptyList(), ExecutableMode.INVOKE)
            }
            .registerType(SqlParserFactory::class.java) { type ->
                type.withConstructor(Collections.emptyList(), ExecutableMode.INVOKE)
            }
            .registerType(Array<UUID>::class.java) { type ->
                type.withConstructor(Collections.emptyList(), ExecutableMode.INVOKE)
            }
    }
}