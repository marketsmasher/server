package com.marketsmasher.util

import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import java.util.UUID
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object Utils {
    fun extractPrincipalId(call: ApplicationCall): UUID? {
        return try {
            UUID.fromString(call.principal<JWTPrincipal>()?.payload?.getClaim("id")?.asString())
        } catch (_: IllegalArgumentException) {
            null
        }
    }

    fun hmacSha256(secret: String, message: String): String {
        val secretKeySpec = SecretKeySpec(secret.toByteArray(), "HmacSHA256")
        val mac = Mac.getInstance("HmacSHA256")
        mac.init(secretKeySpec)
        val hash = mac.doFinal(message.toByteArray())
        return hash.joinToString("") { "%02x".format(it) }
    }
}