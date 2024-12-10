package com.example.online.shop.model

import java.io.Serializable

interface Model<T> : Serializable {

    val formattedValue: T
}