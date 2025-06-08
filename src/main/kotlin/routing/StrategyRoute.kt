package com.marketsmasher.routing

import com.marketsmasher.dto.SubscriptionRequest
import com.marketsmasher.dto.StrategyRequest
import com.marketsmasher.model.Strategy
import com.marketsmasher.model.Subscription
import com.marketsmasher.service.StrategyService
import com.marketsmasher.util.Utils
import io.ktor.http.*
import io.ktor.serialization.*
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
                call.respond(HttpStatusCode.Created, strategyService.addStrategy(strategy).toResponse())

            } catch (ex: IllegalStateException) {
                call.respond(HttpStatusCode.BadRequest, ex.message.toString())
            } catch (ex: BadRequestException) {
                call.respond(HttpStatusCode.BadRequest, ex.message.toString())
            }
        }
        authenticate {
            post("/subscribe") {
                try {
                    val userId = Utils.extractPrincipalId(call)
                    val subscription = call.receive<SubscriptionRequest>().toModel(userId)
                    call.respond(HttpStatusCode.Created, strategyService.addSubscriber(subscription).toResponse())

                } catch (ex: BadRequestException) {
                    call.respond(HttpStatusCode.BadRequest, ex.message.toString())
                } catch (ex: NotFoundException) {
                    call.respond(HttpStatusCode.NotFound, ex.message.toString())
                } catch (ex: JsonConvertException) {
                    call.respond(HttpStatusCode.BadRequest, ex.message.toString())
                } catch (ex: IllegalStateException) {
                    call.respond(HttpStatusCode.BadRequest, ex.message.toString())
                }
            }
            get("/subscribed") {
                val userId = Utils.extractPrincipalId(call)

            }
        }
    }
}