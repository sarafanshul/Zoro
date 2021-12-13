package com.projectdelta.zoro.di

import android.app.Application
import com.projectdelta.zoro.BuildConfig
import com.projectdelta.zoro.util.Constants.CONNECTION_TIMEOUT
import com.projectdelta.zoro.util.Constants.READ_TIMEOUT
import com.projectdelta.zoro.util.Constants.WRITE_TIMEOUT
import com.projectdelta.zoro.util.networking.connectivity.ConnectivityManager
import com.projectdelta.zoro.util.networking.connectivity.ConnectivityManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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
            level = HttpLoggingInterceptor.Level.HEADERS
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

//    @Singleton
//    @Provides
//    fun provideRetrofitBuilder(okHttpClient: OkHttpClient): Retrofit.Builder {
//        return Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .client(okHttpClient)
//            .addCallAdapterFactory(ApiResultCallAdapterFactory())
//            .addConverterFactory(GsonConverterFactory.create())
//    }

}