package com.marketsmasher.routing

import com.marketsmasher.model.User
import com.marketsmasher.dto.UserRequest
import com.marketsmasher.service.BybitService
import com.marketsmasher.service.StrategyService
import com.marketsmasher.service.UserService
import com.marketsmasher.util.Utils
import io.ktor.client.HttpClient
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.auth.authenticate
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.SerializationException
import java.util.UUID

fun Route.userRoute(
    userService: UserService,
    bybitService: BybitService,
    strategyService: StrategyService
) {
    route("/users") {

        get("/list") { call.respond(userService.allUsers().map(User::toResponse)) }

        get("/byId/{id}") {
            try {
                val idString = call.parameters["id"]
                if (idString == null) {
                    call.respond(HttpStatusCode.BadRequest, "Id must be provided")
                    return@get
                }

                val user = userService.userById(UUID.fromString(idString))
                if (user == null) {
                    call.respond(HttpStatusCode.NotFound, "User with given id doesn't exist")
                    return@get
                }

                call.respond(user.toResponse())
            } catch (ex: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, ex.message.toString())
            }
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
                val id = Utils.extractPrincipalId(call)!!
                call.respond(userService.userById(id)!!.toResponse())
            }

            delete("/deleteMe") {
                try {
                    val id = Utils.extractPrincipalId(call)!!

                    strategyService.unsubscribeUserFromEverything(id)
                    userService.removeUser(id)
                    call.respond(HttpStatusCode.NoContent, "User $id has been deleted")
                } catch (ex: NotFoundException) {
                    call.respond(HttpStatusCode.NotFound, ex.message.toString())
                }
            }
        }

        post("/add") {
            try {
                val user = call.receive<UserRequest>().toModel()
                if (!bybitService.newUserEnteredValidCredentials(user))
                    call.respond(HttpStatusCode.BadRequest, "Invalid bybit credentials")

                val response = userService.addUser(user).toResponse()
                call.respond(HttpStatusCode.Created, response)

            } catch (ex: IllegalStateException) {
                call.respond(HttpStatusCode.Conflict, ex.message.toString())
            } catch (_: SerializationException) {
                call.respond(HttpStatusCode.BadRequest, "Invalid bybit credentials")
            }
        }
    }
}
