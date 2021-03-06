/*
 * Copyright (c) 2022. Anshul Saraf
 */

package com.projectdelta.zoro.util.networking.connectivity

import androidx.lifecycle.MutableLiveData

interface ConnectivityManager {

    val isNetworkAvailable: MutableLiveData<Boolean>

    fun registerConnectionObserver()

    fun unregisterConnectionObserver()

}
