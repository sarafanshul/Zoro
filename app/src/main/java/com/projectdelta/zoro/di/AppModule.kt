/*
 * Copyright (c) 2022. Anshul Saraf
 */

package com.projectdelta.zoro.di

import android.content.Context
import com.projectdelta.zoro.ZoroApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): ZoroApplication =
        app as ZoroApplication

}
