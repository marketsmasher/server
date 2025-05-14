package com.marketsmasher.dto

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class StrategyResponse(
    @Contextual
    val id: UUID,
    val name: String,
    val symbol: String
)