package com.marketsmasher.routing

import com.marketsmasher.dto.LoginRequest
import com.marketsmasher.service.JwtService
import io.ktor.http.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authRoute(jwtService: JwtService) {
    route("/auth") {
        post {
            try {
                val loginRequest = call.receive<LoginRequest>()
                val token = jwtService.createJwtToken(loginRequest)
                if (token == null)
                    call.respond(HttpStatusCode.Unauthorized, "Wrong login or password!")

                call.respond(hashMapOf("token" to token))
            } catch (ex: BadRequestException) {
                call.respond(HttpStatusCode.BadRequest, ex.message.toString())
            }
        }
    }
}