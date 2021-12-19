package com.projectdelta.zoro.data.repository

import com.projectdelta.zoro.data.model.Message
import com.projectdelta.zoro.data.model.QueueInformation
import kotlinx.coroutines.flow.Flow

interface MessageRepository {

    suspend fun sendMessage(message: Message) : QueueInformation?

    suspend fun getMessageCount( queueName : String ) : Int

    suspend fun insertMessageToDatabase( message: Message )

    suspend fun updateMessageToDatabase( message: Message )

    suspend fun deleteMessageFromDatabase( message: Message )

    suspend fun getAllMessagesFilteredBySeen( isSeen : Boolean ) : Flow<List<Message>>
}