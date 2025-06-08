package com.marketsmasher.routing

import com.marketsmasher.dto.LoginRequest
import com.marketsmasher.service.JwtService
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.JsonConvertException
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.request.ContentTransformationException
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
                    throw (IllegalArgumentException("Wrong login or password!"))
                }
                call.respond(hashMapOf("token" to token))
            } catch (ex: BadRequestException) {
                call.respond(HttpStatusCode.BadRequest, ex.message.toString())
            } catch (ex: IllegalArgumentException) {
                call.respond(HttpStatusCode.Unauthorized, ex.message.toString())
            }
        }
    }
}