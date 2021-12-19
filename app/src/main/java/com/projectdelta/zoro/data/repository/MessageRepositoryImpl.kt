package com.projectdelta.zoro.data.repository

import android.os.Looper
import com.projectdelta.zoro.data.local.MessageDao
import com.projectdelta.zoro.data.model.Message
import com.projectdelta.zoro.data.model.QueueInformation
import com.projectdelta.zoro.data.remote.MessageApi
import com.projectdelta.zoro.util.NotFound
import com.projectdelta.zoro.util.networking.apiCallAdapter.ApiResult
import kotlinx.coroutines.flow.Flow

class MessageRepositoryImpl (
    private val api: MessageApi ,
    private val dao: MessageDao
) : MessageRepository {

    companion object{
        private const val WRONG_THREAD_EXCEPTION = "Wrong Method Exception, (Required IO thread)"
    }

    @Throws(NotFound.ItsYourFaultIdiotException::class)
    override suspend fun sendMessage(message: Message ) : QueueInformation? {
        if(Thread.currentThread().equals( Looper.getMainLooper().thread))
            throw NotFound.ItsYourFaultIdiotException(WRONG_THREAD_EXCEPTION)

        return when( val result : ApiResult<QueueInformation?> = api.sendMessage(message) ){
            is ApiResult.Success -> result.data
            else -> null
        }
    }

    @Throws(NotFound.ItsYourFaultIdiotException::class)
    override suspend fun getMessageCount( queueName : String ) : Int {
        if(Thread.currentThread().equals( Looper.getMainLooper().thread))
            throw NotFound.ItsYourFaultIdiotException(WRONG_THREAD_EXCEPTION)

        return when( val result : ApiResult<Int?> = api.getMessageCount(queueName) ){
            is ApiResult.Success -> result.data ?: 0
            else -> -1
        }
    }

    @Throws(NotFound.ItsYourFaultIdiotException::class)
    override suspend fun insertMessageToDatabase(message: Message) {
        if(Thread.currentThread().equals( Looper.getMainLooper().thread))
            throw NotFound.ItsYourFaultIdiotException(WRONG_THREAD_EXCEPTION)

        dao.insertMessage(message)
    }

    @Throws(NotFound.ItsYourFaultIdiotException::class)
    override suspend fun updateMessageToDatabase(message: Message) {
        if(Thread.currentThread().equals( Looper.getMainLooper().thread))
            throw NotFound.ItsYourFaultIdiotException(WRONG_THREAD_EXCEPTION)

        dao.updateMessage(message)
    }

    @Throws(NotFound.ItsYourFaultIdiotException::class)
    override suspend fun deleteMessageFromDatabase(message: Message) {
        if(Thread.currentThread().equals( Looper.getMainLooper().thread))
            throw NotFound.ItsYourFaultIdiotException(WRONG_THREAD_EXCEPTION)

        dao.deleteMessage(message)
    }

    @Throws(NotFound.ItsYourFaultIdiotException::class)
    override suspend fun getAllMessagesFilteredBySeen(isSeen: Boolean) : Flow<List<Message>> {
        if(Thread.currentThread().equals( Looper.getMainLooper().thread))
            throw NotFound.ItsYourFaultIdiotException(WRONG_THREAD_EXCEPTION)

        return dao.getAllMessagesFilteredBySeen(isSeen)
    }
}