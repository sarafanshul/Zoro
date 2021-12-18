package com.projectdelta.zoro.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projectdelta.zoro.data.model.MessageData
import com.projectdelta.zoro.util.networking.amqp.AMQPClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.set

@HiltViewModel
class MainViewModel @Inject constructor(
    private val amqpClient: AMQPClient
) : ViewModel(){

    init {
        observeMessages()
    }
    private val _listData : MutableMap<String ,MutableList<MessageData>> = mutableMapOf()
    val listData = MutableSharedFlow< MutableMap<String ,MutableList<MessageData>> >()

    private val _newMessage = MutableSharedFlow<MessageData?>()
    val newMessage = _newMessage.asSharedFlow()

    fun registerClient(){
        viewModelScope.launch(Dispatchers.IO) {
            amqpClient.registerChannel()

            amqpClient.consumeMessage("7") { m ->
                viewModelScope.launch {
                    _newMessage.emit(m)
                }
            }
        }
    }

    private fun observeMessages(){

        viewModelScope.launch(Dispatchers.IO) {
            newMessage.collect {m ->
                if(m?.senderId != null) {
                    if(_listData.containsKey(m.senderId))
                        _listData[m.senderId]?.add(m)
                    else
                        _listData[m.senderId] = mutableListOf(m)

                    viewModelScope.launch {
                        listData.emit(_listData)
                    }
                }
            }
        }
    }

    fun unregisterClient(){
        viewModelScope.launch(Dispatchers.IO) {
            amqpClient.unregisterChannel()
        }
    }

}