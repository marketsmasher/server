package com.marketsmasher.repository

import com.marketsmasher.model.Strategy
import java.util.UUID

class StrategyRepository {
    private val strategies = mutableListOf<Strategy>()

    fun allStrategies() = strategies

    fun strategyById(id: UUID) = strategies.firstOrNull { it.id == id }

    fun strategyByName(name: String) = strategies.firstOrNull { it.name == name }

    fun strategiesBySymbol(symbol: String) = strategies.filter { it.symbol == symbol }

    fun addStrategy(strategy: Strategy) = strategies.add(strategy)
}