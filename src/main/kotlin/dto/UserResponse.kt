package com.marketsmasher.dto

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class UserResponse(
    @Contextual
    val id: UUID,
    val username: String
)