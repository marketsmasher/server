package com.marketsmasher.model

import com.marketsmasher.dto.UserResponse
import java.util.UUID

data class User(
    val id: UUID,
    val username: String,
    val password: String,
    val publicKey: String,
    val privateKey: String
) {
    fun toResponse() = UserResponse(id, username)
}