package com.projectdelta.zoro.di

import android.content.Context
import com.projectdelta.zoro.data.preferences.PreferencesManager
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {

    @Singleton
    @Provides
    fun providePreferenceManager(@ApplicationContext context: Context): PreferencesManager {
        return PreferencesManager(context)
    }

    @EntryPoint
    @Singleton
    @InstallIn(SingletonComponent::class)
    interface PreferenceManagerProviderEntryPoint {
        fun providesPreferenceManager(): PreferencesManager
    }

}