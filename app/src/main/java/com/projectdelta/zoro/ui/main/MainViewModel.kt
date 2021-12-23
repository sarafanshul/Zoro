package com.projectdelta.zoro.ui.main

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projectdelta.zoro.data.model.Message
import com.projectdelta.zoro.data.repository.MessageRepository
import com.projectdelta.zoro.util.networking.amqp.AMQPClient
import com.projectdelta.zoro.util.system.lang.launchIO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val amqpClient: AMQPClient,
    private val messageRepository: MessageRepository
) : ViewModel() {

    private val _newMessage = MutableSharedFlow<Message?>()
    val newMessage = _newMessage.asSharedFlow()

    private val _bottomNavVisibility = MutableSharedFlow<Int>()

    val bottomNavVisibility = _bottomNavVisibility.asSharedFlow()

    fun showBottomNav() {
        launchIO {
            _bottomNavVisibility.emit(View.VISIBLE)
        }
    }

    fun hideBottomNav() {
        launchIO {
            _bottomNavVisibility.emit(View.GONE)
        }
    }

    fun registerClient() {
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

    fun unregisterClient() {
        viewModelScope.launch(Dispatchers.IO) {
            amqpClient.unregisterChannel()
        }
    }

}