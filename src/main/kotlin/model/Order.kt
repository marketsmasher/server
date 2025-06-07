package com.marketsmasher.model


class Order(
    val category: String,
    val symbol: String,
    val side: String,
    val orderType: String,
    val qty: String,
    val marketUnit: String
)