package com.projectdelta.zoro.ui.main.home

import androidx.lifecycle.ViewModel
import com.projectdelta.zoro.data.model.Message
import com.projectdelta.zoro.data.model.User
import com.projectdelta.zoro.data.preferences.PreferencesManager
import com.projectdelta.zoro.data.repository.MessageRepository
import com.projectdelta.zoro.data.repository.UserRepository
import com.projectdelta.zoro.util.system.lang.launchIO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val messageRepository: MessageRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    init {
        getPreferences()
        getUnreadMessages()
    }

    private val _friends = MutableSharedFlow<List<User>>(replay = 1)

    private val _unreadMessages = MutableSharedFlow<List<Message>>(replay = 1)

    val userData = _friends.combine(_unreadMessages){fr ,data ->
        fr.forEach {
            it.messages = 0
        }
        data.forEach x@{ message ->
            fr.forEach y@{ f ->
                if( f.id == message.senderId ){
                    f.messages++
                    return@y
                }
            }
        }
        fr
    }

    private fun getFriends(userId : String ){
        launchIO{
            val data = userRepository.getFriends(userId)
            _friends.emit(data)
        }
    }

    private fun getUnreadMessages(){
        launchIO{
            messageRepository.getAllMessagesFilteredBySeen(false).collectLatest {
                _unreadMessages.emit(it)
            }
        }
    }

    private fun getPreferences(){
        launchIO {
            preferencesManager.preferenceFlow.collectLatest { pref ->
//                getFriends(pref.userId)
                getFriends("7")
            }
        }
    }

}