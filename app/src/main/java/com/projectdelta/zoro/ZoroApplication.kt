/*
 * Copyright (c) 2022. Anshul Saraf
 */

package com.projectdelta.zoro

import android.app.Application
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.StrictMode
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.projectdelta.zoro.ui.ExceptionActivity
import com.projectdelta.zoro.util.CustomDebugTree
import com.projectdelta.zoro.util.system.lang.ExceptionListener
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject


@HiltAndroidApp
class ZoroApplication : Application() ,Configuration.Provider, ExceptionListener {

    @Inject lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(CustomDebugTree())
            setupStrictMode()
        }
        setupExceptionHandler()

    }

    private fun setupStrictMode() {
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectAll()
                .penaltyLog()
                .build()
        )
        StrictMode.setVmPolicy(
            StrictMode.VmPolicy.Builder()
//                .detectNonSdkApiUsage()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .build()
        )
    }

    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        // TODO Make sure you are logging this issue some where like Crashlytics.
        // TODO Also indicate that something went wrong to the user like maybe a dialog or an activity.
        Timber.e(throwable, "Caught uncaught exception at Application level")

        Intent(this, ExceptionActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }.also {
            startActivity(it)
        }

    }

    private fun setupExceptionHandler(){
        Handler(Looper.getMainLooper()).post {
            while (true) {
                try {
                    Looper.loop()
                } catch (e: Throwable) {
                    uncaughtException(Looper.getMainLooper().thread, e)
                }
            }
        }
        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            uncaughtException(t, e)
        }
    }
}
