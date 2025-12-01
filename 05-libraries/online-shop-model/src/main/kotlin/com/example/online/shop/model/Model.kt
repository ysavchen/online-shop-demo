package com.example.online.shop.model

import java.io.Serializable

interface Model<T> : Serializable {

    /**
     * Formatted value from a domain primitive
     */
    val value: T
}