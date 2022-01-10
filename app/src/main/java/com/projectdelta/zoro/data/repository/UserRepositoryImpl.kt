package com.projectdelta.zoro.data.repository

import android.os.Looper
import com.projectdelta.zoro.data.model.ConnectionData
import com.projectdelta.zoro.data.model.User
import com.projectdelta.zoro.data.remote.UserApi
import com.projectdelta.zoro.util.Constants
import com.projectdelta.zoro.util.NotFound
import com.projectdelta.zoro.util.networking.apiCallAdapter.ApiResult

class UserRepositoryImpl(
    private val api: UserApi
) : UserRepository {

    @Throws(NotFound.ItsYourFaultIdiotException::class)
    override suspend fun getUserById(userId: String): User? {
        if (Thread.currentThread().equals(Looper.getMainLooper().thread))
            throw NotFound.ItsYourFaultIdiotException(Constants.WRONG_THREAD_EXCEPTION_IO)

        return when (val result: ApiResult<User?> = api.getUserById(userId)) {
            is ApiResult.Success -> result.data
            else -> null
        }
    }

    @Throws(NotFound.ItsYourFaultIdiotException::class)
    override suspend fun getFriends(userId: String): List<User> {
        if (Thread.currentThread().equals(Looper.getMainLooper().thread))
            throw NotFound.ItsYourFaultIdiotException(Constants.WRONG_THREAD_EXCEPTION_IO)

        return when (val result: ApiResult<List<User>?> = api.getFriends(userId)) {
            is ApiResult.Success -> result.data ?: listOf()
            else -> listOf()
        }
    }

    @Throws(NotFound.ItsYourFaultIdiotException::class)
    override suspend fun connectUser(connectionData: ConnectionData): Boolean {
        if (Thread.currentThread().equals(Looper.getMainLooper().thread))
            throw NotFound.ItsYourFaultIdiotException(Constants.WRONG_THREAD_EXCEPTION_IO)

        return when (val result: ApiResult<Boolean?> = api.connectUser(connectionData)) {
            is ApiResult.Success -> result.data ?: false
            else -> false
        }
    }

    @Throws(NotFound.ItsYourFaultIdiotException::class)
    override suspend fun addUser(user: User): Boolean {
        if (Thread.currentThread().equals(Looper.getMainLooper().thread))
            throw NotFound.ItsYourFaultIdiotException(Constants.WRONG_THREAD_EXCEPTION_IO)

        return when (val result: ApiResult<Boolean?> = api.addUser(user)) {
            is ApiResult.Success -> result.data ?: false
            else -> false
        }
    }

    @Throws(NotFound.ItsYourFaultIdiotException::class)
    override suspend fun disconnectUser(connectionData: ConnectionData): Boolean {
        if (Thread.currentThread().equals(Looper.getMainLooper().thread))
            throw NotFound.ItsYourFaultIdiotException(Constants.WRONG_THREAD_EXCEPTION_IO)

        return when (val result: ApiResult<Boolean?> = api.disconnectUser(connectionData)) {
            is ApiResult.Success -> result.data ?: false
            else -> false
        }
    }
}