/*
 * Copyright (c) 2022. Anshul Saraf
 */

package com.projectdelta.zoro.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.projectdelta.zoro.data.preferences.PreferencesManager
import com.projectdelta.zoro.di.NetworkModule
import com.projectdelta.zoro.di.PreferencesModule
import com.projectdelta.zoro.util.networking.connectivity.ConnectivityManager
import dagger.hilt.android.EntryPointAccessors

abstract class BaseActivity : AppCompatActivity() {

    /**
     * Injects dependencies in classes not supported by Hilt
     * [Refer](https://developer.android.com/training/dependency-injection/hilt-android#not-supported)
     */
    private val connectivityManagerHiltEntryPoint: NetworkModule.ConnectivityManagerProviderEntryPoint by lazy {
        EntryPointAccessors.fromApplication(
            applicationContext,
            NetworkModule.ConnectivityManagerProviderEntryPoint::class.java
        )
    }
    private val preferencesManagerHiltEntryPoint: PreferencesModule.PreferenceManagerProviderEntryPoint by lazy {
        EntryPointAccessors.fromApplication(
            applicationContext,
            PreferencesModule.PreferenceManagerProviderEntryPoint::class.java
        )
    }

    lateinit var connectivityManager: ConnectivityManager
    lateinit var preferenceManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preferenceManager = preferencesManagerHiltEntryPoint.providesPreferenceManager()

        connectivityManager = connectivityManagerHiltEntryPoint.connectivityManager()
        connectivityManager.registerConnectionObserver()

    }

    override fun onDestroy() {
        connectivityManager.unregisterConnectionObserver()
        super.onDestroy()
    }

}
