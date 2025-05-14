package com.marketsmasher.service

import com.marketsmasher.dto.KlinesRequest
import com.marketsmasher.dto.UserRequest
import com.marketsmasher.util.HmacSHA256
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

class BybitService {
    private val httpClient = HttpClient()

    suspend fun getKlines(request: KlinesRequest): HttpResponse {
        println(request)
        val response = httpClient.get("https://api.bybit.com/v5/market/kline") {
            url {
                parameters.apply {
                    request.category?.let { append("category", it) }
                    request.symbol?.let { append("symbol", it) }
                    request.interval?.let { append("interval", it) }
                    request.start?.let { append("start", it) }
                    request.end?.let { append("end", it) }
                    request.limit?.let { append("limit", it) }
                }
            }
        }
        println(response.status)
        return response
    }

    suspend fun validateApiKeys(userRequest: UserRequest): Boolean {
        val timestamp = System.currentTimeMillis().toString()
        val recvWindow = "5000"
        val signString = "$timestamp${userRequest.publicKey}$recvWindow" // Для GET без параметров

        val signature = HmacSHA256.calc(signString, userRequest.privateKey)


        val response = httpClient.get("https://api.bybit.com/v5/account/wallet-balance") {
            headers {
                append("X-BAPI-API-KEY", userRequest.publicKey)
                append("X-BAPI-TIMESTAMP", timestamp)
                append("X-BAPI-SIGN", signature)
                append("X-BAPI-RECV-WINDOW", recvWindow)
            }
        }
        println(response)
        return true
    }
}