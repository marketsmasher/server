package com.marketsmasher.dto

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class SubscriptionRequest(
    @Contextual
    val strategyId: UUID,
    @Contextual
    val userId: UUID
)