package com.marketsmasher.service

import com.marketsmasher.model.User
import com.marketsmasher.repository.UserRepository
import io.ktor.server.plugins.NotFoundException
import java.util.*

class UserService(
    private val userRepository: UserRepository
) {
    fun allUsers() = userRepository.allUsers()
    fun userById(id: UUID) = userRepository.userById(id)
    fun userByUsername(username: String) = userRepository.userByUsername(username)
    fun addUser(user: User): User {
        if (userByUsername(user.username) != null)
            throw IllegalStateException("Cannot duplicate usernames!")

        return userRepository.addUser(user)
    }

    fun removeUser(userId: UUID) {
        if (!userRepository.removeUser(userId))
            throw NotFoundException("User with given id doesn't exist")
    }
}