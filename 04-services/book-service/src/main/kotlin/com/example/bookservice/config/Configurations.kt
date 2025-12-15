package com.example.bookservice.config

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ImportRuntimeHints

@Configuration
@ImportRuntimeHints(AppRuntimeHints::class)
class ApplicationConfiguration
