package com.marketsmasher.dto

import com.marketsmasher.model.Strategy
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class StrategyRequest(
    val name: String,
    val password: String,
    val symbol: String,
    val publicName: String,
    val description: String
) {
    fun toModel() = Strategy(
        id = UUID.randomUUID(),
        name = name,
        password = password,
        symbol = symbol,
        publicName = publicName,
        description = description,
        subscriptions = mutableListOf()
    )
}