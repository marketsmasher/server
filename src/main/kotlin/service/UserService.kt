package com.marketsmasher.service

import com.marketsmasher.model.User
import com.marketsmasher.repository.UserRepository
import java.util.UUID

class UserService(
    private val userRepository: UserRepository
) {
    fun allUsers() = userRepository.allUsers()

    fun userById(id: UUID) = userRepository.userById(id)

    fun userByUsername(username: String) = userRepository.userByUsername(username)

    fun addUser(user: User) {
        if (userByUsername(user.username) != null)
            throw IllegalStateException("Cannot duplicate usernames!")
        if (userById(user.id) != null)
            throw IllegalStateException("Cannot duplicate ids! Bad luck, lol")

        userRepository.addUser(user)
    }

}