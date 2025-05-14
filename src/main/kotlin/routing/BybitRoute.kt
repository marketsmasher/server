package com.marketsmasher.routing

import com.marketsmasher.dto.KlinesRequest
import com.marketsmasher.service.BybitService
import io.ktor.client.call.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.bybitRoute(bybitService: BybitService) {
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

            call.respond(bybitService.getKlines(request).body<String>())
        }
    }
}