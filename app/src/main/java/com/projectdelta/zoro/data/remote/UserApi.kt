package com.projectdelta.zoro.data.remote

import com.projectdelta.zoro.data.model.User
import com.projectdelta.zoro.util.networking.apiCallAdapter.ApiResult
import com.projectdelta.zoro.util.networking.NetworkingConstants.QUERY_ID
import com.projectdelta.zoro.util.networking.NetworkingConstants.SUB_URL_ADD_USER
import com.projectdelta.zoro.util.networking.NetworkingConstants.SUB_URL_GET
import com.projectdelta.zoro.util.networking.NetworkingConstants.USER_URL
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserApi {

    @GET(USER_URL + SUB_URL_GET)
    suspend fun getUserById(
        @Query(QUERY_ID) userId : String
    ) : ApiResult<User?>

    @POST(USER_URL + SUB_URL_ADD_USER)
    suspend fun updateUser(
        @Body user : User
    ) : ApiResult<User?>
}