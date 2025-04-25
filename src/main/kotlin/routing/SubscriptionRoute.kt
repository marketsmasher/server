package com.marketsmasher.routing

import com.marketsmasher.dto.SubscriptionRequest
import com.marketsmasher.service.SubscriptionService
import io.ktor.http.*
import io.ktor.serialization.JsonConvertException
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.subscriptionRoute(subscriptionService: SubscriptionService) {
    route("/subscriptions") {
        post {
            try {
                val request = call.receive<SubscriptionRequest>()
                subscriptionService.addSubscriber(request.strategyId, request.userId)
                call.respond(HttpStatusCode.Created, request)
            } catch (ex: IllegalStateException) {
                call.respond(HttpStatusCode.BadRequest)
            } catch (ex: JsonConvertException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        get {
            call.respond(subscriptionService.allSubscriptions())
        }
    }
}