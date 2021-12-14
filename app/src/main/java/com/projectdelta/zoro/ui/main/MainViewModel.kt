package com.projectdelta.zoro.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projectdelta.zoro.data.model.MessageData
import com.projectdelta.zoro.util.networking.AMQPClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val amqpClient: AMQPClient
) : ViewModel(){

    val data : MutableLiveData<MessageData?> = MutableLiveData()

    fun registerClient(){
        viewModelScope.launch(Dispatchers.IO) {
            amqpClient.registerChannel()

            amqpClient.consumeMessage("1231") { m ->
                data.postValue(m)
            }
        }
    }

    fun unregisterClient(){
        viewModelScope.launch(Dispatchers.IO) {
            amqpClient.unregisterChannel()
        }
    }

}