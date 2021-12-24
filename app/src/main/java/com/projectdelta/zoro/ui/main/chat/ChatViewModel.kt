package com.projectdelta.zoro.ui.main.chat

import androidx.lifecycle.ViewModel
import com.projectdelta.zoro.data.model.Message
import com.projectdelta.zoro.data.preferences.PreferencesManager
import com.projectdelta.zoro.data.repository.MessageRepository
import com.projectdelta.zoro.util.system.lang.getValueOrNull
import com.projectdelta.zoro.util.system.lang.launchIO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

/*
Experimental state...
 */
@HiltViewModel
class ChatViewModel @Inject constructor(
    private val messageRepository: MessageRepository,
    private val preferencesManager: PreferencesManager,
) : ViewModel() {

    private val _incomingMessages = MutableStateFlow<List<Message>>(listOf())
    private val _outgoingMessages = MutableStateFlow<List<Message>>(listOf())

    val messages = _incomingMessages.combine(_outgoingMessages) { m1, m2 ->
        val newList = mutableListOf<Message>()
        newList.addAll(m1)
        newList.addAll(m2)
        newList.sortBy { it.time }
        newList
    }

    fun getReceiverUserData(userId: String) {
        launchIO {
            messageRepository.getAllMessagesFilteredBySeenAndSender(userId, false).collectLatest {
                _incomingMessages.emit(it)
            }
        }
    }

    // TODO move to activityViewModel
    fun sendMessage(message: Message) {
        launchIO {
            val newMessage = messageRepository.sendMessage(message)
            // update backend to return new message with timestamp
            /* if( newMessage != null )
                message.time = new time*/
            // here assuming message has timestamp.
            val messageList: MutableList<Message> =
                _outgoingMessages.getValueOrNull()?.toMutableList() ?: mutableListOf()
            messageList.add(message)
            _outgoingMessages.emit(messageList)
        }
    }



}