/*
 * Copyright (c) 2022. Anshul Saraf
 */

package com.projectdelta.zoro.data.remote

import com.projectdelta.zoro.data.model.Message
import com.projectdelta.zoro.data.model.MessageReturn
import com.projectdelta.zoro.util.networking.NetworkingConstants.MESSAGE_URL
import com.projectdelta.zoro.util.networking.NetworkingConstants.QUERY_QUEUE
import com.projectdelta.zoro.util.networking.NetworkingConstants.SUB_URL_COUNT
import com.projectdelta.zoro.util.networking.NetworkingConstants.SUB_URL_SEND
import com.projectdelta.zoro.util.networking.apiCallAdapter.ApiResult
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MessageApi {

    @POST(MESSAGE_URL + SUB_URL_SEND)
    suspend fun sendMessage(
        @Body message: Message
    ): ApiResult<MessageReturn?>

    @GET(MESSAGE_URL + SUB_URL_COUNT)
    suspend fun getMessageCount(
        @Query(QUERY_QUEUE) queueName: String
    ): ApiResult<Int?>

}
