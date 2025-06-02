package com.marketsmasher.service

import com.marketsmasher.dto.KlinesRequest
import com.marketsmasher.dto.UserRequest
import com.marketsmasher.model.User
import com.marketsmasher.repository.UserRepository
import com.marketsmasher.util.Utils
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import java.util.UUID

class BybitService(private val userRepository: UserRepository) {
    private val httpClient = HttpClient()

    suspend fun getKlines(request: KlinesRequest): HttpResponse {
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

    suspend fun getWalletBalance(id: UUID, coin: String? = null): HttpResponse {
        val byBitHeaders = generateHeaders(id)
        return httpClient.get("https://api.bybit.com/v5/account/wallet-balance") {
            headers { byBitHeaders.forEach { (key, value) -> append(key, value) } }
            coin?.let { parameter("coin", it) }
        }
    }

    suspend fun validateCredentials(userRequest: UserRequest) {
        println(userRequest)
        val timestamp = System.currentTimeMillis().toString()
        val recvWindow = "5000"
        val signString = "timestamp=$timestamp&api_key=${userRequest.publicKey}&recv_window=$recvWindow"
        val signature = Utils.hmacSha256(userRequest.privateKey, signString)
        println(httpClient.get("https://api.bybit.com/v5/account/wallet-balance") {
            headers {
                append("X-BAPI-API-KEY", userRequest.publicKey)
                append("X-BAPI-TIMESTAMP", timestamp)
                append("X-BAPI-SIGN", signature)
                append("X-BAPI-RECV-WINDOW", recvWindow)
            }
            parameter("accountType", "UNIFIED")
        })
    }

    fun generateHeaders(id: UUID): Map<String, String> {
        val user = userRepository.userById(id)!!
        val timestamp = System.currentTimeMillis().toString()
        val recvWindow = "5000"
        val signString = "timestamp=$timestamp&api_key=${user.publicKey}&recv_window=$recvWindow"
        println(signString)
        val signature = Utils.hmacSha256(user.privateKey, signString)
        return mapOf(
            "X-BAPI-API-KEY" to user.publicKey,
            "X-BAPI-TIMESTAMP" to timestamp,
            "X-BAPI-SIGN" to signature,
            "X-BAPI-RECV-WINDOW" to recvWindow
        )
    }
}