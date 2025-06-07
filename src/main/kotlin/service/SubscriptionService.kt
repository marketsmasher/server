package com.marketsmasher.service

import com.marketsmasher.repository.StrategyRepository
import com.marketsmasher.repository.SubscriptionRepository
import com.marketsmasher.repository.UserRepository
import java.util.*

class SubscriptionService(
    private val subscriptionRepository: SubscriptionRepository,
    private val strategyRepository: StrategyRepository,
    private val userRepository: UserRepository
) {
    fun allSubscriptions() = subscriptionRepository.allSubscriptions()

    fun subscribersById(strategyId: UUID) = subscriptionRepository.subscribersById(strategyId)

    fun subscriptionsById(userId: UUID) =
        allSubscriptions()
            .filter { (_, subscribers) -> userId in subscribers }
            .map { (strategyId, _) -> strategyId }

    fun addSubscriber(strategyId: UUID, userId: UUID) {
        if (strategyRepository.strategyById(strategyId) == null)
            throw IllegalStateException("Strategy with provided id doesn't exist!")
        if (userRepository.userById(userId) == null)
            throw IllegalStateException("User with provided id doesn't exist!")
        if (isUserSubscribed(strategyId, userId))
            throw IllegalStateException("User cannot be subscribed on the same strategy several times!")

        subscriptionRepository.addSubscriber(strategyId, userId)
    }

    private fun isUserSubscribed(strategyId: UUID, userId: UUID) =
        subscribersById(strategyId)?.contains(userId) ?: false
}