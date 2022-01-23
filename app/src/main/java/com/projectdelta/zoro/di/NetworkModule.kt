/*
 * Copyright (c) 2022. Anshul Saraf
 */

package com.projectdelta.zoro.di

import android.app.Application
import com.google.gson.Gson
import com.projectdelta.zoro.BuildConfig
import com.projectdelta.zoro.data.remote.MessageApi
import com.projectdelta.zoro.data.remote.UserApi
import com.projectdelta.zoro.util.Constants.CONNECTION_TIMEOUT
import com.projectdelta.zoro.util.Constants.READ_TIMEOUT
import com.projectdelta.zoro.util.Constants.WRITE_TIMEOUT
import com.projectdelta.zoro.util.networking.NetworkingConstants.BASE_URL
import com.projectdelta.zoro.util.networking.NetworkingConstants.RABBIT_API
import com.projectdelta.zoro.util.networking.NetworkingConstants.RABBIT_PORT
import com.projectdelta.zoro.util.networking.NetworkingConstants.RABBIT_USER_NAME
import com.projectdelta.zoro.util.networking.NetworkingConstants.RABBIT_USER_PASSWORD
import com.projectdelta.zoro.util.networking.amqp.AMQPClient
import com.projectdelta.zoro.util.networking.amqp.RabbitMQClient
import com.projectdelta.zoro.util.networking.apiCallAdapter.ApiResultCallAdapterFactory
import com.projectdelta.zoro.util.networking.connectivity.ConnectivityManager
import com.projectdelta.zoro.util.networking.connectivity.ConnectivityManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideConnectivityManager(application: Application): ConnectivityManager {
        return ConnectivityManagerImpl(application)
    }

    @EntryPoint
    @Singleton
    @InstallIn(SingletonComponent::class)
    interface ConnectivityManagerProviderEntryPoint {
        fun connectivityManager(): ConnectivityManager
    }

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder().apply {
            connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            retryOnConnectionFailure(true) // change to false if paging issue
            if (BuildConfig.DEBUG)
                addNetworkInterceptor(loggingInterceptor)
        }.build()
    }

    @Singleton
    @Provides
    fun provideAMQPClient(gson: Gson): AMQPClient {
        return RabbitMQClient.getInstance(
            host = RABBIT_API,
            port = RABBIT_PORT,
            uName = RABBIT_USER_NAME,
            password = RABBIT_USER_PASSWORD,
            deserializer = gson
        )
    }

    @Singleton
    @Provides
    fun provideRetrofitBuilder(okHttpClient: OkHttpClient): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(ApiResultCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
    }

    @Singleton
    @Provides
    fun provideMessageApiService(retrofitBuilder: Retrofit.Builder): MessageApi {
        return retrofitBuilder
            .build()
            .create(MessageApi::class.java)
    }

    @Singleton
    @Provides
    fun provideUserApiService(retrofitBuilder: Retrofit.Builder): UserApi {
        return retrofitBuilder
            .build()
            .create(UserApi::class.java)
    }

}
