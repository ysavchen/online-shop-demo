package com.example.online.shop.model.validation

open class ModelValidationException(message: String) : IllegalArgumentException(message)
class IsbnValidationException(message: String) : ModelValidationException(message)