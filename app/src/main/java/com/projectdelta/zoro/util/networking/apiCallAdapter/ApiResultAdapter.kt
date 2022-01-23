/*
 * Copyright (c) 2022. Anshul Saraf
 */

package com.projectdelta.zoro.util.networking.apiCallAdapter

import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class ApiResultAdapter(
    private val type: Type
) : CallAdapter<Type, Call<ApiResult<Type>>> {
    override fun responseType() = type
    override fun adapt(call: Call<Type>): Call<ApiResult<Type>> = ApiResultCall(call)
}
