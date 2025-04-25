package com.marketsmasher.routing

import com.marketsmasher.dto.StrategyRequest
import com.marketsmasher.model.Strategy
import com.marketsmasher.service.StrategyService
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.JsonConvertException
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.strategyRoute(strategyService: StrategyService) {
    route("/strategies") {
        post {
            try {
                val strategy = call.receive<StrategyRequest>().toModel()
                strategyService.addStrategy(strategy)
                call.respond(HttpStatusCode.Created, strategy.toResponse())
            } catch (ex: IllegalStateException) {
                call.respond(HttpStatusCode.BadRequest)
            } catch (ex: JsonConvertException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        get {
            call.respond(strategyService.allStrategies().map(Strategy::toResponse))
        }
    }
}