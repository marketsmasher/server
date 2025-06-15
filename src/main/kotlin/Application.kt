package com.marketsmasher

import com.marketsmasher.plugins.configureRouting
import com.marketsmasher.plugins.configureSecurity
import com.marketsmasher.plugins.configureSerialization
import com.marketsmasher.repository.StrategyRepository
import com.marketsmasher.repository.UserRepository
import com.marketsmasher.service.BybitService
import com.marketsmasher.service.JwtService
import com.marketsmasher.service.StrategyService
import com.marketsmasher.service.UserService
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val userRepository = UserRepository()
    val userService = UserService(userRepository)

    val strategyRepository = StrategyRepository()
    val strategyService = StrategyService(strategyRepository, userService)

    val jwtService = JwtService(this, userService)

    val bybitService = BybitService(strategyService, userService)

    configureSerialization()
    configureSecurity(jwtService)
    configureRouting(userService, strategyService, jwtService, bybitService)
}
