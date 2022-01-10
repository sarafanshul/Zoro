package com.projectdelta.zoro.ui.main.home

import androidx.lifecycle.ViewModel
import com.projectdelta.zoro.data.model.Message
import com.projectdelta.zoro.data.model.User
import com.projectdelta.zoro.data.preferences.PreferencesManager
import com.projectdelta.zoro.data.repository.MessageRepository
import com.projectdelta.zoro.data.repository.UserRepository
import com.projectdelta.zoro.util.system.lang.copy
import com.projectdelta.zoro.util.system.lang.getValueBlockedOrNull
import com.projectdelta.zoro.util.system.lang.getValueOrNull
import com.projectdelta.zoro.util.system.lang.launchIO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val messageRepository: MessageRepository,
    preferencesManager: PreferencesManager
) : ViewModel() {

    val userId = preferencesManager.preferenceFlow.getValueBlockedOrNull()?.userId

    init {
        getPreferences()
        getUnreadMessages()
    }


    private val _friends = MutableStateFlow<List<User>>(listOf())
    private val _apiResponse = MutableStateFlow<List<User>>(listOf())

    private val _unreadMessages = MutableStateFlow<List<Message>>(listOf())

    private val _searchChanel = MutableStateFlow("")

    val userData = _friends.combine(_unreadMessages) { fr, data ->
        fr.forEach {
            it.messagesCount = 0
            it.lastMessage = ""
        }
        data.forEach x@{ message ->
            fr.forEach y@{ f ->
                if (f.id == message.senderId) {
                    f.messagesCount++
                    f.lastMessage = message.data ?: ""
                    return@y
                }
            }
        }
        fr.copy()
    }

    private fun getFriends(userId: String) {
        launchIO {
            val data = userRepository.getFriends(userId)
            _apiResponse.emit(data)
            observeSearch() // because we do not observe value of _apiResponse
        }
    }

    private fun getUnreadMessages() {
        launchIO {
            messageRepository.getAllMessagesFilteredBySeen(false).collectLatest {
                _unreadMessages.emit(it)
            }
        }
    }

    fun getPreferences() {
        getFriends(userId!!)
    }

    fun updateQuery(query: String) {
        launchIO {
            _searchChanel.emit(query)
        }
    }

    private fun observeSearch() {
        launchIO {
            _searchChanel.collectLatest { query ->
                Timber.d("Search Query : $query")
                val newData = _apiResponse.getValueOrNull()?.filter { user ->
                    user.name?.contains(query, ignoreCase = true) ?: false
                } ?: listOf()
                Timber.d(newData.toString())
                launchIO {
                    _friends.emit(newData)
                }
            }
        }
    }

}