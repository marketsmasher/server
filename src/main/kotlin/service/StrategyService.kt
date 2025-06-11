package com.marketsmasher.service

import com.marketsmasher.model.Strategy
import com.marketsmasher.model.Subscription
import com.marketsmasher.repository.StrategyRepository
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
        check(strategyByName(strategy.name) != null) {
            "Cannot duplicate strategy names!"
        }
        return strategyRepository.addStrategy(strategy)
    }

    fun strategiesByUserId(userId: UUID) = allStrategies()
        .filter { strategy -> strategy.allSubscriptions().any { it.userId == userId } }
        .map { it.id }

    fun userBasisPointsSum(userId: UUID) = allStrategies()
        .mapNotNull { strategy -> strategy.allSubscriptions().firstOrNull { it.userId == userId } }
        .sumOf { it.shareBasisPoint }

    fun addSubscription(strategyId: UUID, subscription: Subscription): Subscription {
        val strategy = strategyById(strategyId)
        if (strategy == null)
            throw NotFoundException("Strategy with given id not found")

        val user = userService.userById(subscription.userId)
        if (user == null)
            throw NotFoundException("User with given id not found")

        check(!strategy.allSubscriptions().any { it.userId == user.id }) {
            "User ${user.toResponse()} is already subscribed to this strategy"
        }

        check(userBasisPointsSum(user.id) + subscription.shareBasisPoint <= 10000) {
            "Cannot allocate more than 100% of funds"
        }

        return subscription.also { strategy.addSubscription(subscription) }
    }

    fun removeSubscription(strategyId: UUID, userId: UUID): Boolean {
        if (userService.userById(userId) == null)
            throw NotFoundException("User with given id not found")

        return strategyById(strategyId)?.removeSubscriber(userId)
            ?: throw NotFoundException("Strategy with given id not found")
    }

    fun unsubscribeUserFromEverything(userId: UUID) {
        if (userService.userById(userId) == null)
            throw NotFoundException("User with given id not found")

        strategiesByUserId(userId)
            .mapNotNull { strategyById(it) }
            .map { it.removeSubscriber(userId) }
    }
}