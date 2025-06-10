package com.marketsmasher.model

import com.marketsmasher.dto.StrategyResponse
import java.util.*

class Strategy(
    val id: UUID,
    val name: String,
    val password: String,
    val symbol: String,
    val publicName: String,
    val description: String,
    private val subscriptions: MutableList<Subscription>
) {
    fun toResponse() = StrategyResponse(
        id = id,
        name = name,
        symbol = symbol,
        publicName = publicName,
        description = description
    )

    fun allSubscriptions() = subscriptions.toList()
    fun addSubscription(subscription: Subscription) = subscription.also { subscriptions.add(it) }
    fun removeSubscriber(userId: UUID) = subscriptions.removeIf { it.userId == userId }
}