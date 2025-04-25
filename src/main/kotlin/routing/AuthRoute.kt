package com.marketsmasher.routing

import com.marketsmasher.dto.LoginRequest
import com.marketsmasher.service.JwtService
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.JsonConvertException
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.authRoute(jwtService: JwtService) {
    route("/auth") {
        post {
            try {
                val loginRequest = call.receive<LoginRequest>()
                val token = jwtService.createJwtToken(loginRequest)
                if (token == null) {
                    call.respond(HttpStatusCode.Unauthorized)
                    return@post
                }
                call.respond(hashMapOf("token" to token))
            } catch (ex: JsonConvertException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}