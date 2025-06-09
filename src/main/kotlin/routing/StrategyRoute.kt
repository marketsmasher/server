package com.marketsmasher.routing

import com.marketsmasher.dto.StrategyRequest
import com.marketsmasher.dto.SubscriptionRequest
import com.marketsmasher.model.Strategy
import com.marketsmasher.service.StrategyService
import com.marketsmasher.util.Utils
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.strategyRoute(strategyService: StrategyService) {
    route("/strategies") {

        get("/list") {
            call.respond(strategyService.allStrategies().map(Strategy::toResponse))
        }

        post("/add") {
            try {
                val strategy = call.receive<StrategyRequest>().toModel()
                val response = strategyService.addStrategy(strategy).toResponse()
                call.respond(HttpStatusCode.Created, response)

            } catch (ex: IllegalStateException) {
                call.respond(HttpStatusCode.Conflict, ex.message.toString())
            } catch (ex: BadRequestException) {
                call.respond(HttpStatusCode.BadRequest, ex.message.toString())
            }
        }

        authenticate {
            post("/subscribe") {
                try {
                    val subscriptionRequest = call.receive<SubscriptionRequest>()

                    val strategyId = subscriptionRequest.strategyId
                    val subscription = subscriptionRequest.toModel(Utils.extractPrincipalId(call)!!)
                    val response = strategyService.addSubscription(strategyId, subscription)
                    call.respond(HttpStatusCode.Created, response)

                } catch (ex: BadRequestException) {
                    call.respond(HttpStatusCode.BadRequest, ex.message.toString())
                } catch (ex: NotFoundException) {
                    call.respond(HttpStatusCode.NotFound, ex.message.toString())
                }
            }

            delete("/unsubscribe") {  }

            get("/subscribed") {
                val userId = Utils.extractPrincipalId(call)

            }
        }
    }
}