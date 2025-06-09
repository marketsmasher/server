package com.marketsmasher.service

import com.marketsmasher.model.Strategy
import com.marketsmasher.model.Subscription
import com.marketsmasher.repository.StrategyRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.plugins.*
import java.util.*

class StrategyService(
    private val strategyRepository: StrategyRepository, private val userService: UserService
) {
    fun allStrategies() = strategyRepository.allStrategies()
    fun strategyById(strategyId: UUID) = strategyRepository.strategyById(strategyId)
    fun strategyByName(name: String) = strategyRepository.strategyByName(name)
    fun strategiesBySymbol(symbol: String) = strategyRepository.strategiesBySymbol(symbol)

    fun addStrategy(strategy: Strategy): Strategy {
        if (strategyByName(strategy.name) != null)
            throw IllegalStateException("Cannot duplicate strategy names!")

        return strategyRepository.addStrategy(strategy)
    }

    fun addSubscription(strategyId: UUID, subscription: Subscription): Subscription {
        val user = userService.userById(subscription.userId)
        if (user == null)
            throw NotFoundException("User with given id not found")

        val strategy = strategyById(strategyId)
        if (strategy == null)
            throw NotFoundException("Strategy with given id not found")

        if (strategy.subscriptions.contains(subscription))
            throw IllegalStateException("User cannot be subscribed on the same strategy several times!")

        return subscription.also { strategy.subscriptions.add(it) }
    }

    fun removeSubscription(strategyId: UUID, userId: UUID): HttpStatusCode {
        if (userService.userById(userId) == null)
            throw NotFoundException("User with given id not found")

        val strategy = strategyById(strategyId)
        if (strategy == null)
            throw NotFoundException("Strategy with given id not found")

        strategy.subscriptions.removeAll { it.userId == userId }

        return HttpStatusCode.NoContent
    }
}