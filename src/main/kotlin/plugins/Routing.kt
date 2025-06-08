package com.marketsmasher.plugins

import com.marketsmasher.routing.authRoute
import com.marketsmasher.routing.bybitRoute
import com.marketsmasher.routing.strategyRoute
import com.marketsmasher.routing.userRoute
import com.marketsmasher.service.BybitService
import com.marketsmasher.service.JwtService
import com.marketsmasher.service.StrategyService
import com.marketsmasher.service.UserService
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    userService: UserService,
    strategyService: StrategyService,
    jwtService: JwtService,
    bybitService: BybitService
) {
    routing {
        route("/api/v1") {
            authRoute(jwtService)
            userRoute(userService, bybitService)
            strategyRoute(strategyService)
            bybitRoute(bybitService, userService, strategyService)
        }
    }
}