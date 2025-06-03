package com.marketsmasher.service

import com.marketsmasher.dto.KlinesRequest
import com.marketsmasher.repository.UserRepository
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.text.Charsets.UTF_8

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
        val timestamp = System.currentTimeMillis().toString()
        val publicKey = userRepository.userById(id)!!.publicKey
        val privateKey = userRepository.userById(id)!!.privateKey
        val recvWindow = "5000"
        val queryString = "accountType=UNIFIED${coin?.let { "&coin=$it" } ?: ""}"
        val signature = generateSignature(timestamp, privateKey, publicKey, recvWindow, queryString)
        val response = httpClient.get("https://api.bybit.com/v5/account/wallet-balance") {
            parameter("accountType", "UNIFIED")
            coin?.let { parameter("coin", it) }

            headers {
                append("X-BAPI-API-KEY", publicKey)
                append("X-BAPI-SIGN", signature)
                append("X-BAPI-TIMESTAMP", timestamp)
                append("X-BAPI-RECV-WINDOW", recvWindow)
                append("Content-Type", "application/json")
            }
        }

        return response
    }


    private fun generateSignature(
        timestamp: String,
        privateKey: String,
        publicKey: String,
        recvWindow: String,
        payload: String
    ): String {
        val paramStr = timestamp + publicKey + recvWindow + payload
        val secretKeySpec = SecretKeySpec(privateKey.toByteArray(UTF_8), "HmacSHA256")
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