package com.marketsmasher.service

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.marketsmasher.dto.LoginRequest
import io.ktor.server.application.Application
import io.ktor.server.auth.jwt.JWTCredential
import io.ktor.server.auth.jwt.JWTPrincipal
import java.util.Date
import java.util.UUID

class JwtService(private val application: Application, private val userService: UserService) {

    private val secret = getConfigProperty("jwt.secret")
    private val issuer = getConfigProperty("jwt.issuer")
    private val audience = getConfigProperty("jwt.audience")

    val realm = getConfigProperty("jwt.realm")

    val jwtVerifier: JWTVerifier = JWT
        .require(Algorithm.HMAC256(secret))
        .withAudience(audience)
        .withIssuer(issuer)
        .build()

    fun createJwtToken(loginRequest: LoginRequest): String? {
        val foundUser = userService.userByUsername(loginRequest.username)

        return if (foundUser != null && foundUser.password == loginRequest.password)
            JWT
                .create()
                .withAudience(audience)
                .withIssuer(issuer)
                .withClaim("id", foundUser.id.toString())
                .withExpiresAt(Date(System.currentTimeMillis() + 3_600_000))
                .sign(Algorithm.HMAC256(secret))
        else
            null
    }

    fun customValidator(credential: JWTCredential): JWTPrincipal? {
        val id = UUID.fromString(credential.payload.getClaim("id").asString())
        val foundUser = id?.let(userService::userById)

        return foundUser?.let {
            if (credential.payload.audience.contains(audience)) {
                JWTPrincipal(credential.payload)
            } else null
        }
    }

    private fun getConfigProperty(path: String) = application.environment.config.property(path).getString()
}