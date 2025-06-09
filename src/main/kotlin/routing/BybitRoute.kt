package com.marketsmasher.routing

import com.marketsmasher.dto.KlinesRequest
import com.marketsmasher.dto.OrderRequest
import com.marketsmasher.service.BybitService
import com.marketsmasher.service.StrategyService
import com.marketsmasher.service.UserService
import com.marketsmasher.util.Utils
import io.ktor.client.call.*
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlin.math.abs

fun Route.bybitRoute(
    bybitService: BybitService,
    userService: UserService,
    strategyService: StrategyService
) {
    route("/bybit") {
        get("/market/kline") {
            val request = KlinesRequest(
                category = call.parameters["category"],
                symbol = call.parameters["symbol"],
                interval = call.parameters["interval"],
                start = call.parameters["start"],
                end = call.parameters["end"],
                limit = call.parameters["limit"]
            )
            val response = bybitService.getKlines(request)
            call.respond(response.body<String>())
        }

        authenticate {
            get("/account/wallet-balance") {
                val id = Utils.extractPrincipalId(call)!!
                val coin = call.parameters["coin"]
                val response = bybitService.getWalletBalance(userService.userById(id)!!, coin)
                call.respond(response.body<String>())
            }
        }

        post("/order/create") {
            try {
                val orderRequest = call.receive<OrderRequest>()
                if (strategyService.strategyById(orderRequest.strategyId) == null) {
                    call.respond(HttpStatusCode.NotFound, "Strategy with given id doesn't exist")
                    return@post
                }
                if (abs(orderRequest.confidence) > 1.0) {
                    call.respond(HttpStatusCode.BadRequest, "Confidence must be between -1.0 and 1.0")
                    return@post
                }
                bybitService.placeOrders(orderRequest)
            } catch (_: JsonConvertException) {
                call.respond(HttpStatusCode.BadRequest, "Parsing error occurred")
            }
        }
    }
}