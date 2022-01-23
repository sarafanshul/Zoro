/*
 * Copyright (c) 2022. Anshul Saraf
 */

package com.projectdelta.zoro.di

import com.projectdelta.zoro.data.local.MessageDao
import com.projectdelta.zoro.data.remote.MessageApi
import com.projectdelta.zoro.data.remote.UserApi
import com.projectdelta.zoro.data.repository.MessageRepository
import com.projectdelta.zoro.data.repository.MessageRepositoryImpl
import com.projectdelta.zoro.data.repository.UserRepository
import com.projectdelta.zoro.data.repository.UserRepositoryImpl
import com.projectdelta.zoro.di.qualifiers.IODispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideMessageRepository(
        api: MessageApi,
        messageDao: MessageDao,
        @IODispatcher dispatcher : CoroutineDispatcher
    ): MessageRepository {
        return MessageRepositoryImpl(api, messageDao, dispatcher)
    }

    @Singleton
    @Provides
    fun provideUserRepository(api: UserApi): UserRepository {
        return UserRepositoryImpl(api)
    }

}
