package com.marketsmasher.plugins

import com.marketsmasher.util.UUIDSerializer
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

val appJson = Json {
    ignoreUnknownKeys = true
    prettyPrint = true
    isLenient = true
    serializersModule = SerializersModule {
        contextual(UUIDSerializer)
    }
}

fun Application.configureSerialization() {
    install(ContentNegotiation) { json(appJson) }
}

//fun HttpClientConfig<*>.configureJsonSerialization() {
//    install(ContentNegotiation) { json(appJson) }
//}
