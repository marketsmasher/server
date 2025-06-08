package com.marketsmasher.service

import com.marketsmasher.model.Strategy
import com.marketsmasher.model.Subscription
import com.marketsmasher.repository.StrategyRepository
import com.marketsmasher.repository.UserRepository
import io.ktor.server.plugins.NotFoundException
import java.util.UUID

class StrategyService(
    private val strategyRepository: StrategyRepository,
    private val userRepository: UserRepository
) {
    fun allStrategies() = strategyRepository.allStrategies()
    fun strategyById(strategyId: UUID) = strategyRepository.strategyById(strategyId)
    fun strategyByName(name: String) = strategyRepository.strategyByName(name)
    fun strategiesBySymbol(symbol: String) = strategyRepository.strategiesBySymbol(symbol)

    fun addStrategy(strategy: Strategy): Strategy {
        if (strategyByName(strategy.name) != null)
            throw IllegalStateException("Cannot duplicate strategy names!")
        if (strategyById(strategy.id) != null)
            throw IllegalStateException("Cannot duplicate ids! Bad luck, lol")

        return strategyRepository.addStrategy(strategy)
    }

    fun addSubscriber(subscription: Subscription): Strategy {
//        val strategy = strategyById(strategyId)
//        if (strategy == null)
//            throw NotFoundException("Strategy with given id not found")
//        if (userRepository.userById(userId) == null)
//            throw NotFoundException("User with given id not found")
//        if (strategy.subscribers.contains(userId))
//            throw IllegalStateException("Cannot subscribe on the same strategy several times!")
//
//        return strategy.also { it.subscribers.add(userId) }
        TODO()
    }


}