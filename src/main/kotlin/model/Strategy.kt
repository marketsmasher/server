package com.marketsmasher.model

import com.marketsmasher.dto.StrategyResponse
import java.util.UUID

class Strategy(
    val id: UUID,
    val name: String,
    val password: String,
    val symbol: String
) {
    fun toResponse() = StrategyResponse(id, name, symbol)
}