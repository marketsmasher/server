package com.marketsmasher.dto

import com.marketsmasher.model.Subscription
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class SubscriptionRequest(
    @Contextual
    val strategyId: UUID,
    val shareBasisPoint: Int
) {
    fun toModel(userId: UUID) = Subscription(
        userId = userId,
        shareBasisPoint = shareBasisPoint
    )
}