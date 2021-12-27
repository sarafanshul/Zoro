package com.projectdelta.zoro.data.repository

import com.projectdelta.zoro.data.model.Message
import com.projectdelta.zoro.data.model.MessageReturn
import kotlinx.coroutines.flow.Flow

interface MessageRepository {

    suspend fun sendMessage(message: Message): MessageReturn?

    suspend fun getMessageCount(queueName: String): Int

    suspend fun insertMessageToDatabase(message: Message)

    suspend fun updateMessageToDatabase(message: Message)

    suspend fun updateMessageByUserIdSeen(userId : String ,seen: Boolean)

    suspend fun deleteMessageFromDatabase(message: Message)

    suspend fun getAllMessagesFilteredBySeen(isSeen: Boolean): Flow<List<Message>>

    suspend fun getAllMessagesFilteredBySeenAndSender(
        senderId: String,
        seen: Boolean
    ): Flow<List<Message>>

    suspend fun getAllMessagesFilteredBySeenAndSenderOffline(
        senderId: String,
        seen: Boolean
    ) : List<Message>

    suspend fun deleteAllMessagesFilteredBySeen(isSeen: Boolean)
}