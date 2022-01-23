/*
 * Copyright (c) 2022. Anshul Saraf
 */

package com.projectdelta.zoro.di

import android.app.Application
import com.projectdelta.zoro.data.local.MessageDao
import com.projectdelta.zoro.data.local.MessageDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideMessageDatabase(application: Application): MessageDao {
        return MessageDatabase.getInstance(application).messageDao()
    }
}
