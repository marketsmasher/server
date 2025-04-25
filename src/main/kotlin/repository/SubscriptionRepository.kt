package com.marketsmasher.repository

import java.util.UUID

class SubscriptionRepository {
    private val subscriptions = mutableMapOf<UUID, MutableList<UUID>>()

    fun allSubscriptions() = subscriptions

    fun subscribersById(strategyId: UUID) = subscriptions[strategyId]

    fun addSubscriber(strategyId: UUID, userId: UUID) =
        subscriptions.getOrPut(strategyId) { mutableListOf() }.add(userId)
}