package com.marketsmasher.service

import com.marketsmasher.dto.KlinesRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request

class BybitService {
    private val httpClient = HttpClient()

    suspend fun getKlines(request: KlinesRequest): HttpResponse {
        println(request)
        val response = httpClient.get("https://api.bybit.com/v5/market/kline") {
            url {
                parameters.apply {
                    request.category?.let { append("category", it) }
                    append("symbol", request.symbol)
                    append("interval", request.interval)
                    request.start?.let { append("start", it) }
                    request.end?.let { append("end", it) }
                    request.limit?.let { append("limit", it) }
                }
            }
        }
        println(response.status)
        return response
    }
}