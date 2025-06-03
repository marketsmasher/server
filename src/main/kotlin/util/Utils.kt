package com.marketsmasher.util

import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import java.util.UUID
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object Utils {
    fun extractPrincipalId(call: ApplicationCall): UUID =
        UUID.fromString(call.principal<JWTPrincipal>()!!.payload.getClaim("id").asString())
}