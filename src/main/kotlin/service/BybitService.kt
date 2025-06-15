package com.marketsmasher.service

import com.marketsmasher.dto.KlinesRequest
import com.marketsmasher.dto.OrderRequest
import com.marketsmasher.model.Order
import com.marketsmasher.model.User
import com.marketsmasher.repository.StrategyRepository
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.server.application.*
import io.ktor.server.plugins.NotFoundException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.text.Charsets.UTF_8

class BybitService(
    private val strategyService: StrategyService,
    private val userService: UserService
) {
    private val httpClient = HttpClient()
    private val url = when (System.getenv()["NETWORK_NAME"]) {
        "mainnet" -> "https://api.bybit.com/v5"
        "testnet" -> "https://api-testnet.bybit.com/v5"
        else -> "https://api-testnet.bybit.com/v5"
    }.also { println("Market api: $it") }

    suspend fun getKlines(request: KlinesRequest): HttpResponse = httpClient.get("$url/market/kline") {
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

    suspend fun newUserEnteredValidCredentials(user: User): Boolean {
        val jsonBody = getWalletBalance(user).bodyAsText()
        val jsonElement = Json.parseToJsonElement(jsonBody)
        return jsonElement.jsonObject["retCode"]?.toString() == "0"
    }

    suspend fun getWalletBalance(user: User, coin: String? = null): HttpResponse {
        val queryString = "accountType=UNIFIED${coin?.let { "&coin=$it" } ?: ""}"

        return httpClient.get("$url/account/wallet-balance") {
            parameter("accountType", "UNIFIED")
            coin?.let { parameter("coin", it) }
            headers {
                generateHeaders(
                    user,
                    "10000", queryString
                ).forEach { (key, value) -> append(key, value) }
            }
        }
    }

    suspend fun placeOrders(orderRequest: OrderRequest): List<HttpResponse> {
        val strategy = strategyService.strategyByName(orderRequest.strategyName)
            ?: throw NotFoundException("Strategy with such name doesn't exist")

        return strategy.allSubscriptions().map {
            val order = orderRequest.toModel(strategy.symbol, it.shareBasisPoint / 10000.0)
            val user = userService.userById(it.userId) ?: error("User not found")

            httpClient.post("$url/order/create") {
                contentType(ContentType.Application.Json)
                setBody(order)
                headers { generateHeaders(user, "10000", Json.encodeToString(order)) }
            }
        }
    }

    private fun generateHeaders(user: User, recvWindow: String, queryString: String): Map<String, String> {
        val timestamp = System.currentTimeMillis().toString()

        return mapOf(
            "X-BAPI-API-KEY" to user.publicKey,
            "X-BAPI-SIGN" to generateSignature(timestamp, user, queryString, recvWindow),
            "X-BAPI-TIMESTAMP" to timestamp,
            "X-BAPI-RECV-WINDOW" to recvWindow
        )
    }

    private fun generateSignature(timestamp: String, user: User, payload: String, recvWindow: String): String {
        val paramStr = "$timestamp${user.publicKey}$recvWindow$payload"
        val secretKeySpec = SecretKeySpec(user.privateKey.toByteArray(UTF_8), "HmacSHA256")
        val mac = Mac.getInstance("HmacSHA256").apply { init(secretKeySpec) }
        val hash = mac.doFinal(paramStr.toByteArray(UTF_8))
        return bytesToHex(hash)
    }

    private fun bytesToHex(hash: ByteArray): String {
        val hexString = StringBuilder()
        for (b in hash) {
            val hex = Integer.toHexString(0xff and b.toInt())
            if (hex.length == 1) hexString.append('0')
            hexString.append(hex)
        }
        return hexString.toString()
    }
}