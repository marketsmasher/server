package com.marketsmasher.routing

import com.marketsmasher.dto.StrategyRequest
import com.marketsmasher.dto.StrategyResponse
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
import java.util.UUID

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

            } catch (ex: BadRequestException) {
                call.respond(HttpStatusCode.BadRequest, ex.message.toString())
            } catch (ex: IllegalStateException) {
                call.respond(HttpStatusCode.Conflict, ex.message.toString())
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
                } catch (ex: IllegalStateException) {
                    call.respond(HttpStatusCode.Conflict, ex.message.toString())
                }
            }

            delete("/unsubscribe/{strategyId}") {
                try {
                    println("!!!!!")
                    val strategyId = UUID.fromString(call.parameters["strategyId"])
                    println(strategyId)
                    println("!!!!!")
                    if (strategyId == null) {
                        call.respond(HttpStatusCode.BadRequest, "Strategy id must be provided")
                        return@delete
                    }

                    val userId = Utils.extractPrincipalId(call)!!
                    if (strategyService.removeSubscription(strategyId, userId))
                        call.respond(HttpStatusCode.NoContent)
                    else
                        call.respond(HttpStatusCode.NotFound, "User hasn't been subscribed")


                } catch (ex: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest, ex.message.toString())
                } catch (ex: NotFoundException) {
                    call.respond(HttpStatusCode.NotFound, ex.message.toString())
                }
            }

            get("/subscribed") {
                call.respond(
                    strategyService
                        .strategiesByUserId(Utils.extractPrincipalId(call)!!)
                        .mapNotNull { strategyService.strategyById(it) }
                        .map { it.toResponse() }
                )
            }
        }
    }
}