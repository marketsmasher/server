package com.marketsmasher.dto

import com.marketsmasher.model.Order
import kotlinx.serialization.Serializable

@Serializable
data class OrderRequest(
    val strategyName: String,
    val password: String,
    val confidence: Double
) {
    fun toModel(symbol: String, share: Double) = Order(
        category = "spot",
        symbol = symbol,
        side = if (confidence >= 0.0) "Buy" else "Sell",
        orderType = "Market",
        qty = (confidence * share).toString(),
        marketUnit = if (confidence >= 0.0) "baseCoin" else "quoteCoin"
    )
}