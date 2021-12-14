package com.projectdelta.zoro.data.repository

import com.projectdelta.zoro.data.model.MessageData
import com.projectdelta.zoro.data.model.QueueInformation

interface MessageRepository {

    suspend fun sendMessage( messageData: MessageData) : QueueInformation?

    suspend fun getMessageCount( queueName : String ) : Int
}