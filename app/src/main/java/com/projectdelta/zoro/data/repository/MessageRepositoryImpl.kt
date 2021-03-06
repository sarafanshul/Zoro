/*
 * Copyright (c) 2022. Anshul Saraf
 */

package com.projectdelta.zoro.data.repository

import android.os.Looper
import com.projectdelta.zoro.data.local.MessageDao
import com.projectdelta.zoro.data.model.Message
import com.projectdelta.zoro.data.model.MessageReturn
import com.projectdelta.zoro.data.remote.MessageApi
import com.projectdelta.zoro.di.qualifiers.IODispatcher
import com.projectdelta.zoro.util.Constants.WRONG_THREAD_EXCEPTION_IO
import com.projectdelta.zoro.util.NotFound
import com.projectdelta.zoro.util.networking.apiCallAdapter.ApiResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class MessageRepositoryImpl(
    private val api: MessageApi,
    private val dao: MessageDao,
    @IODispatcher private val workerDispatcher : CoroutineDispatcher,
) : MessageRepository {

    @Throws(NotFound.ItsYourFaultIdiotException::class)
    override suspend fun sendMessage(message: Message): MessageReturn? {
        if (Thread.currentThread().equals(Looper.getMainLooper().thread))
            throw NotFound.ItsYourFaultIdiotException(WRONG_THREAD_EXCEPTION_IO)

        return when (val result: ApiResult<MessageReturn?> = api.sendMessage(message)) {
            is ApiResult.Success -> result.data
            else -> null
        }
    }

    @Throws(NotFound.ItsYourFaultIdiotException::class)
    override suspend fun getMessageCount(queueName: String): Int {
        if (Thread.currentThread().equals(Looper.getMainLooper().thread))
            throw NotFound.ItsYourFaultIdiotException(WRONG_THREAD_EXCEPTION_IO)

        return when (val result: ApiResult<Int?> = api.getMessageCount(queueName)) {
            is ApiResult.Success -> result.data ?: 0
            else -> -1
        }
    }

    @Throws(NotFound.ItsYourFaultIdiotException::class)
    override suspend fun insertMessageToDatabase(message: Message) {
        if (Thread.currentThread().equals(Looper.getMainLooper().thread))
            throw NotFound.ItsYourFaultIdiotException(WRONG_THREAD_EXCEPTION_IO)

        dao.insertMessage(message)
    }

    @Throws(NotFound.ItsYourFaultIdiotException::class)
    override suspend fun updateMessageToDatabase(message: Message) {
        if (Thread.currentThread().equals(Looper.getMainLooper().thread))
            throw NotFound.ItsYourFaultIdiotException(WRONG_THREAD_EXCEPTION_IO)

        dao.updateMessage(message)
    }

    @Throws(NotFound.ItsYourFaultIdiotException::class)
    override suspend fun updateMessageByUserIdSeen(userId: String, seen: Boolean) {
        if (Thread.currentThread().equals(Looper.getMainLooper().thread))
            throw NotFound.ItsYourFaultIdiotException(WRONG_THREAD_EXCEPTION_IO)

        dao.updateMessageByUserIdSeen(userId, seen)
    }

    @Throws(NotFound.ItsYourFaultIdiotException::class)
    override suspend fun deleteMessageFromDatabase(message: Message) {
        if (Thread.currentThread().equals(Looper.getMainLooper().thread))
            throw NotFound.ItsYourFaultIdiotException(WRONG_THREAD_EXCEPTION_IO)

        dao.deleteMessage(message)
    }

    @Throws(NotFound.ItsYourFaultIdiotException::class)
    override suspend fun getAllMessagesFilteredBySeen(isSeen: Boolean): Flow<List<Message>> {
        if (Thread.currentThread().equals(Looper.getMainLooper().thread))
            throw NotFound.ItsYourFaultIdiotException(WRONG_THREAD_EXCEPTION_IO)

        return dao
            .getAllMessagesFilteredBySeen(isSeen)
            .flowOn(workerDispatcher)
    }

    @Throws(NotFound.ItsYourFaultIdiotException::class)
    override suspend fun getAllMessagesFilteredBySeenAndSender(
        senderId: String,
        seen: Boolean
    ): Flow<List<Message>> {
        if (Thread.currentThread().equals(Looper.getMainLooper().thread))
            throw NotFound.ItsYourFaultIdiotException(WRONG_THREAD_EXCEPTION_IO)

        return dao
            .getAllMessagesFilteredBySeenAndSender(senderId, seen)
            .flowOn(workerDispatcher)
    }

    override suspend fun getAllMessagesFilteredBySeenAndSenderOffline(
        senderId: String,
        seen: Boolean
    ): List<Message> {
        if (Thread.currentThread().equals(Looper.getMainLooper().thread))
            throw NotFound.ItsYourFaultIdiotException(WRONG_THREAD_EXCEPTION_IO)

        return dao.getAllMessagesFilteredBySeenAndSenderOffline(senderId, seen)
    }

    @Throws(NotFound.ItsYourFaultIdiotException::class)
    override suspend fun deleteAllMessagesFilteredBySeen(isSeen: Boolean) {
        if (Thread.currentThread().equals(Looper.getMainLooper().thread))
            throw NotFound.ItsYourFaultIdiotException(WRONG_THREAD_EXCEPTION_IO)

        dao.deleteAllMessagesFilteredBySeen(isSeen)
    }
}
