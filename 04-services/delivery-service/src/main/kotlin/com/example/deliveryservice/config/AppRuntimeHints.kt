package com.example.deliveryservice.config

import liquibase.database.LiquibaseTableNamesFactory
import liquibase.parser.SqlParserFactory
import liquibase.report.ShowSummaryGeneratorFactory
import liquibase.ui.LoggerUIService
import org.springframework.aot.hint.ExecutableMode
import org.springframework.aot.hint.RuntimeHints
import org.springframework.aot.hint.RuntimeHintsRegistrar
import java.util.*

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
    }
}