package com.marketsmasher.repository

import com.marketsmasher.model.User
import java.util.*

class UserRepository {
    private val users = mutableListOf<User>()

    fun allUsers() = users.toList()
    fun userById(id: UUID): User? = users.firstOrNull { it.id == id }
    fun userByUsername(username: String): User? = users.firstOrNull { it.username == username }
    fun addUser(user: User) = user.also { users.add(it) }
    fun removeUser(userId: UUID) = users.removeIf { it.id == userId }
}