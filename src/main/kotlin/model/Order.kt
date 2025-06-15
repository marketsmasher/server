package com.marketsmasher.model

import kotlinx.serialization.Serializable

@Serializable
class Order(
    val category: String,
    val symbol: String,
    val side: String,
    val orderType: String,
    val qty: String,
    val marketUnit: String
)