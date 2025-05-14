package com.marketsmasher.routing

import com.marketsmasher.dto.KlinesRequest
import com.marketsmasher.service.BybitService
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.bybitRoute(bybitService: BybitService) {
    route("/bybit") {
        get("/market/kline") {
            val symbol = call.parameters["symbol"] ?: return@get call.respond(
                status = HttpStatusCode.BadRequest,
                message = "Parameter \"symbol\" is obligatory"
            )

            val interval = call.parameters["interval"] ?: return@get call.respond(
                status = HttpStatusCode.BadRequest,
                message = "Parameter \"interval\" is obligatory"
            )

            val request = KlinesRequest(
                category = call.parameters["category"],
                symbol = symbol,
                interval = interval,
                start = call.parameters["start"],
                end = call.parameters["end"],
                limit = call.parameters["limit"]
            )

            call.respond(bybitService.getKlines(request).body<String>())
        }
    }
}