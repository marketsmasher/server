package com.marketsmasher.routing

import com.marketsmasher.model.User
import com.marketsmasher.dto.UserRequest
import com.marketsmasher.service.UserService
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.UUID

fun Route.userRoute(userService: UserService) {
    route("/users") {

        get { call.respond(userService.allUsers().map(User::toResponse)) }

        get("byId/{id}") {
            val id = call.parameters["id"]
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            val user = userService.userById(UUID.fromString(id))
            if (user == null) {
                call.respond(HttpStatusCode.NotFound)
                return@get
            }

            call.respond(user.toResponse())
        }

        get("byUsername/{username}") {
            val username = call.parameters["username"]
            if (username == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            val user = userService.userByUsername(username)
            if (user == null) {
                call.respond(HttpStatusCode.NotFound)
                return@get
            }

            call.respond(user.toResponse())
        }

        post("/add") {
            try {
                val user = call.receive<UserRequest>().toModel()
                userService.addUser(user)
                call.respond(HttpStatusCode.Created, user.toResponse())
            } catch (_: IllegalStateException) {
                call.respond(HttpStatusCode.BadRequest)
            } catch (_: JsonConvertException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

    }
}
