package com.marketsmasher.model

import java.util.UUID

class Subscription(
    val userId: UUID,
    val strategyId: UUID,
    val baseCoinQty: Double,
    val quoteCoinQty: Double
)