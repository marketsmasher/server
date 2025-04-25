package com.marketsmasher.service

import com.marketsmasher.model.Strategy
import com.marketsmasher.repository.StrategyRepository
import java.util.UUID

class StrategyService(
    private val strategyRepository: StrategyRepository
) {
    fun allStrategies() = strategyRepository.allStrategies()

    fun strategyById(strategyId: UUID) = strategyRepository.strategyById(strategyId)

    fun strategyByName(name: String) = strategyRepository.strategyByName(name)

    fun strategiesBySymbol(symbol: String) = strategyRepository.strategiesBySymbol(symbol)

    fun addStrategy(strategy: Strategy) {
        if (strategyByName(strategy.name) != null)
            throw IllegalStateException("Cannot duplicate strategy names!")
        if (strategyById(strategy.id) != null)
            throw IllegalStateException("Cannot duplicate ids! Bad luck, lol")

        strategyRepository.addStrategy(strategy)
    }
}