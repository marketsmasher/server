package com.marketsmasher.plugins

import com.marketsmasher.util.UUIDSerializer
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(
            Json { serializersModule = SerializersModule { contextual(UUIDSerializer) } }
        )
    }
}
