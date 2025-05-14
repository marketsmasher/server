package com.marketsmasher.plugins

import com.marketsmasher.routing.authRoute
import com.marketsmasher.routing.bybitRoute
import com.marketsmasher.routing.strategyRoute
import com.marketsmasher.routing.subscriptionRoute
import com.marketsmasher.routing.userRoute
import com.marketsmasher.service.BybitService
import com.marketsmasher.service.JwtService
import com.marketsmasher.service.StrategyService
import com.marketsmasher.service.SubscriptionService
import com.marketsmasher.service.UserService
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    userService: UserService,
    strategyService: StrategyService,
    subscriptionService: SubscriptionService,
    jwtService: JwtService,
    bybitService: BybitService
) {
    routing {
        route("/api/v1") {
            authRoute(jwtService)
            userRoute(userService)
            strategyRoute(strategyService)
            subscriptionRoute(subscriptionService)
            bybitRoute(bybitService)
        }
    }
}