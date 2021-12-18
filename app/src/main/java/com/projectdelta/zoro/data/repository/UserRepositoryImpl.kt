package com.projectdelta.zoro.data.repository

import com.projectdelta.zoro.data.model.User
import com.projectdelta.zoro.data.remote.UserApi
import com.projectdelta.zoro.util.networking.apiCallAdapter.ApiResult

class UserRepositoryImpl(
    private val api : UserApi
) : UserRepository {

    override suspend fun getUserById(userId: String): User? {
        return when( val result : ApiResult<User?> = api.getUserById(userId)){
            is ApiResult.Success -> result.data
            else -> null
        }
    }

    override suspend fun updateUser(user: User): User? {
        return when( val result : ApiResult<User?> = api.updateUser(user)){
            is ApiResult.Success -> result.data
            else -> null
        }
    }

    override suspend fun getFriends(userId: String): List<User> {
        return when( val result : ApiResult<List<User>?> = api.getFriends(userId) ){
            is ApiResult.Success -> result.data ?: listOf()
            else -> listOf()
        }
    }
}