package com.projectdelta.zoro.di

import com.projectdelta.zoro.data.remote.MessageApi
import com.projectdelta.zoro.data.remote.UserApi
import com.projectdelta.zoro.data.repository.MessageRepository
import com.projectdelta.zoro.data.repository.MessageRepositoryImpl
import com.projectdelta.zoro.data.repository.UserRepository
import com.projectdelta.zoro.data.repository.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideMessageRepository( api : MessageApi ) : MessageRepository{
        return MessageRepositoryImpl(api)
    }

    @Singleton
    @Provides
    fun provideUserRepository( api : UserApi ) : UserRepository{
        return UserRepositoryImpl(api)
    }

}