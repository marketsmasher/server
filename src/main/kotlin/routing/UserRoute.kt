package com.marketsmasher.routing

import com.marketsmasher.model.User
import com.marketsmasher.dto.UserRequest
import com.marketsmasher.service.BybitService
import com.marketsmasher.service.UserService
import com.marketsmasher.util.Utils
import io.ktor.client.HttpClient
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.auth.authenticate
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.SerializationException
import java.util.UUID

fun Route.userRoute(userService: UserService, bybitService: BybitService) {
    route("/users") {

        get("/list") {
            call.respond(userService.allUsers().map(User::toResponse))
        }

        get("/byId/{id}") {
            val id = call.parameters["id"]
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Id must be provided")
                return@get
            }

            val user = userService.userById(UUID.fromString(id))
            if (user == null) {
                call.respond(HttpStatusCode.NotFound, "User with given id doesn't exist")
                return@get
            }

            call.respond(user.toResponse())
        }

        get("/byUsername/{username}") {
            val username = call.parameters["username"]
            if (username == null) {
                call.respond(HttpStatusCode.BadRequest, "Username must be provided")
                return@get
            }

            val user = userService.userByUsername(username)
            if (user == null) {
                call.respond(HttpStatusCode.NotFound, "User with given username doesn't exist")
                return@get
            }

            call.respond(user.toResponse())
        }

        authenticate {
            get("/byToken") {
                val id = Utils.extractPrincipalId(call)
                call.respond(userService.userById(id)!!.toResponse())
            }
        }

        post("/add") {
            try {
                val user = call.receive<UserRequest>().toModel()
                if (!bybitService.newUserEnteredValidCredentials(user)) {
                    throw BadRequestException("Invalid bybit credentials")
                }
                call.respond(HttpStatusCode.Created, userService.addUser(user).toResponse())

            } catch (ex: BadRequestException) {
                call.respond(HttpStatusCode.BadRequest, ex.message.toString())
            } catch (ex: IllegalStateException) {
                call.respond(HttpStatusCode.BadRequest, ex.message.toString())
            } catch (_: SerializationException) {
                call.respond(HttpStatusCode.BadRequest, "Invalid bybit credentials")
            }
        }
    }
}
