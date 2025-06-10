package com.marketsmasher.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Subscription(
    @Contextual
    val userId: UUID,
    val shareBasisPoint: Int
)