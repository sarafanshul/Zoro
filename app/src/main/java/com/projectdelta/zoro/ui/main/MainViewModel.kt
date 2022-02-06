/*
 * Copyright (c) 2022. Anshul Saraf
 */

package com.projectdelta.zoro.ui.main

import android.view.View
import androidx.lifecycle.ViewModel
import com.projectdelta.zoro.data.model.Message
import com.projectdelta.zoro.data.model.User
import com.projectdelta.zoro.data.repository.MessageRepository
import com.projectdelta.zoro.util.networking.amqp.AMQPClient
import com.projectdelta.zoro.util.system.lang.launchIO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val amqpClient: AMQPClient,
    private val messageRepository: MessageRepository
) : ViewModel() {

    companion object {
        enum class RefreshType {
            CONNECTION_LIST,
        }
    }

    @Volatile
    var currentChatReceiver = ""

    private val _newMessage = MutableSharedFlow<Message?>()
    val newMessage = _newMessage.asSharedFlow()

    private val _bottomNavVisibility = MutableSharedFlow<Int>(replay = 1)

    val bottomNavVisibility = _bottomNavVisibility.asSharedFlow()

    val refreshConnectionList = MutableSharedFlow<RefreshType>() // setting replay = 1 , solves the problem of reload

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

    /**
     * @param queueId [User.id]
     */
    fun registerClient(queueId: String) {
        launchIO {
            amqpClient.registerAndConsume(queueId){ m ->
                launchIO {
                    messageRepository.insertMessageToDatabase(m!!)
                    if (m.senderId != currentChatReceiver) // don't fire while in room with same user
                        _newMessage.emit(m)
                }
            }
        }
    }

    fun unregisterClient() {
        launchIO {
            amqpClient.unregisterChannel()
        }
    }

    fun setMessagesSeen(userId: String) {
        launchIO {
            messageRepository.updateMessageByUserIdSeen(userId, true)
        }
    }

}
