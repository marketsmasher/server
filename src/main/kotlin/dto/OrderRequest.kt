package com.marketsmasher.dto

import com.marketsmasher.model.Order
import com.marketsmasher.service.StrategyService
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class OrderRequest(
    @Contextual
    val strategyName: String,
    val password: String,
    val confidence: Double
) {
//    fun toModel(strategyService: StrategyService, qty: Double): Order? {
//        val strategy = strategyService.strategyById(strategyId)
//        return if (strategy == null) null
//        else
//            Order(
//                category = "spot",
//                symbol = strategy.symbol,
//                side = if (confidence >= 0.0) "Buy" else "Sell",
//                orderType = "Market",
//                qty = (confidence * qty).toString(),
//                marketUnit = if (confidence >= 0.0) "baseCoin" else "quoteCoin"
//            )
//    }
}