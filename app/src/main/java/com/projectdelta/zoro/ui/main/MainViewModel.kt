package com.projectdelta.zoro.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projectdelta.zoro.data.model.Message
import com.projectdelta.zoro.data.repository.MessageRepository
import com.projectdelta.zoro.util.networking.amqp.AMQPClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.set

@HiltViewModel
class MainViewModel @Inject constructor(
    private val amqpClient: AMQPClient ,
    private val messageRepository: MessageRepository
) : ViewModel(){

    init {
        observeMessages()
    }
    private val _listData : MutableMap<String ,MutableList<Message>> = mutableMapOf()
    val listData = MutableSharedFlow< MutableMap<String ,MutableList<Message>> >()

    private val _newMessage = MutableSharedFlow<Message?>()
    val newMessage = _newMessage.asSharedFlow()

    fun registerClient(){
        viewModelScope.launch(Dispatchers.IO) {
            amqpClient.registerChannel()

            amqpClient.consumeMessage("7") { m ->
                viewModelScope.launch(Dispatchers.IO) {
                    messageRepository.insertMessageToDatabase(m!!)
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