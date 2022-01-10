package com.projectdelta.zoro.data.repository

import com.projectdelta.zoro.data.model.ConnectionData
import com.projectdelta.zoro.data.model.User

interface UserRepository {

    suspend fun getUserById(userId: String): User?

    suspend fun connectUser(connectionData: ConnectionData): Boolean

    suspend fun getFriends(userId: String): List<User>

    suspend fun addUser(user: User): Boolean

    suspend fun disconnectUser(connectionData: ConnectionData): Boolean
}