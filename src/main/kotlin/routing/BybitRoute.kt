package com.marketsmasher.routing

import com.marketsmasher.dto.KlinesRequest
import com.marketsmasher.service.BybitService
import com.marketsmasher.util.Utils
import io.ktor.client.call.*
import io.ktor.server.auth.*
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
            println(request)
            val response = bybitService.getKlines(request)
            call.respond(response.body<String>())
        }

        authenticate {
            get("/account/wallet-balance") {
                println("!!!!!!")
                val id = Utils.extractPrincipalId(call)
                val coin = call.parameters["coin"]
                println(id)
                val response = bybitService.getWalletBalance(id, coin)
                call.respond(response.body<String>())
            }
        }
    }
}