package com.projectdelta.zoro.ui.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projectdelta.zoro.data.model.User
import com.projectdelta.zoro.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    init {
        getFriends("7")
    }

    private val _friends = MutableSharedFlow<List<User>>(replay = 1)
    val friends = _friends.asSharedFlow()

    fun getFriends( userId : String ){
        viewModelScope.launch(Dispatchers.IO){
            val data = userRepository.getFriends(userId)
            _friends.emit(data)
        }
    }

}