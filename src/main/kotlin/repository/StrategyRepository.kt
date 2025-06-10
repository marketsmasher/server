package com.marketsmasher.repository

import com.marketsmasher.dto.StrategyRequest
import com.marketsmasher.model.Strategy
import com.marketsmasher.model.Subscription
import io.ktor.server.plugins.NotFoundException
import java.util.UUID

class StrategyRepository {
    private val strategies = mutableListOf<Strategy>(
        StrategyRequest(
            name = "strategy0",
            password = "12345",
            symbol = "BTCUSDT",
            publicName = "Супер прибыльная стратегия PREMIUM PLUS",
            description = "Прорывная стратегия, которая принесёт очень много денег"
        ).toModel(),
        StrategyRequest(
            name = "strategy1",
            password = "12345",
            symbol = "BTCUSDT",
            publicName = "Стратегия средней прибыльности",
            description = "Стратегия, которая подходит начинающим инвесторам"
        ).toModel(),
        StrategyRequest(
            name = "strategy2",
            password = "12345",
            symbol = "ETHUSDT",
            publicName = "Откровенно плохая стратегия",
            description = "Стратегия, которая подходит инвесторам, которым откровенно нечего терять"
        ).toModel(),
        StrategyRequest(
            name = "strategy3",
            password = "12345",
            symbol = "DOGEUSDT",
            publicName = "Стратегия Илона Маска",
            description = "Стратегия, которая которая сделала самого богатого человека самым богатым человеком. Илон маск рекомендует!"
        ).toModel()
    )

    fun allStrategies() = strategies.toList()
    fun strategyById(id: UUID) = strategies.firstOrNull { it.id == id }
    fun strategyByName(name: String) = strategies.firstOrNull { it.name == name }
    fun strategiesBySymbol(symbol: String) = strategies.filter { it.symbol == symbol }
    fun addStrategy(strategy: Strategy) = strategy.also { strategies.add(it) }
    fun addSubscription(strategyId: UUID, subscription: Subscription) =
        strategyById(strategyId)?.addSubscription(subscription)
            ?: throw NotFoundException("Strategy with provided id doesn't exist")

    fun removeSubscriber(strategyId: UUID, userId: UUID) =
        strategyById(strategyId)?.removeSubscriber(userId)
            ?: throw NotFoundException("Strategy with provided id doesn't exist")
}