package com.marketsmasher.util

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import java.util.*

object Utils {
    fun extractPrincipalId(call: ApplicationCall): UUID =
        UUID.fromString(call.principal<JWTPrincipal>()!!.payload.getClaim("id").asString())
}