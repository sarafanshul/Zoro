package com.projectdelta.zoro.data.remote

import com.projectdelta.zoro.data.model.MessageData
import com.projectdelta.zoro.data.model.QueueInformation
import com.projectdelta.zoro.util.networking.apiCallAdapter.ApiResult
import com.projectdelta.zoro.util.networking.NetworkingConstants.MESSAGE_URL
import com.projectdelta.zoro.util.networking.NetworkingConstants.QUERY_QUEUE
import com.projectdelta.zoro.util.networking.NetworkingConstants.SUB_URL_COUNT
import com.projectdelta.zoro.util.networking.NetworkingConstants.SUB_URL_SEND
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MessageApi {

    @POST(MESSAGE_URL + SUB_URL_SEND)
    suspend fun sendMessage(
        @Body messageData: MessageData
    ) : ApiResult<QueueInformation?>

    @GET(MESSAGE_URL + SUB_URL_COUNT)
    suspend fun getMessageCount(
        @Query(QUERY_QUEUE) queueName : String
    ) : ApiResult<Int?>

}