package com.marketsmasher.dto

import com.marketsmasher.model.Subscription
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class SubscriptionRequest(
    @Contextual
    val strategyId: UUID,
    val baseCoinQty: Double,
    val quoteCoinQty: Double
) {
    fun toModel(userId: UUID) = Subscription(
        userId = userId,
        baseCoinQty = baseCoinQty,
        quoteCoinQty = quoteCoinQty
    )
}