package com.marketsmasher.repository

import com.marketsmasher.dto.UserRequest
import com.marketsmasher.model.User
import java.util.UUID

class UserRepository {
    private val users = mutableListOf<User>(
        UserRequest(
            "aboba",
            "qwerty",
            "c0BsvdHmcMUUAyaugm",
            "qxCrms4po0UMbVKRdghBL6p3IXDG53CDNfTR"
        ).toModel()
    )

    fun allUsers() = users
    fun userById(id: UUID): User? = users.firstOrNull { it.id == id }
    fun userByUsername(username: String): User? = users.firstOrNull { it.username == username }
    fun addUser(user: User) = users.add(user)
}