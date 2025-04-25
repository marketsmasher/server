package com.marketsmasher.dto

import com.marketsmasher.model.User
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class UserRequest(
    val username: String,
    val password: String,
    val publicKey: String,
    val privateKey: String
) {
    fun toModel() = User(
        id = UUID.randomUUID(),
        username = username,
        password = password,
        publicKey = publicKey,
        privateKey = privateKey
    )
}
