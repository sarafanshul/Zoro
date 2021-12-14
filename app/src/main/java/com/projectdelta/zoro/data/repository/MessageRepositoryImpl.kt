package com.projectdelta.zoro.data.repository

import com.projectdelta.zoro.data.model.MessageData
import com.projectdelta.zoro.data.model.QueueInformation
import com.projectdelta.zoro.data.remote.MessageApi
import com.projectdelta.zoro.util.networking.apiCallAdapter.ApiResult

class MessageRepositoryImpl (
    private val api: MessageApi
) : MessageRepository {

    override suspend fun sendMessage( messageData: MessageData ) : QueueInformation? {
        return when( val result : ApiResult<QueueInformation?> = api.sendMessage(messageData) ){
            is ApiResult.Success -> result.data
            else -> null
        }
    }

    override suspend fun getMessageCount( queueName : String ) : Int {
        return when( val result : ApiResult<Int?> = api.getMessageCount(queueName) ){
            is ApiResult.Success -> result.data ?: 0
            else -> -1
        }
    }

}