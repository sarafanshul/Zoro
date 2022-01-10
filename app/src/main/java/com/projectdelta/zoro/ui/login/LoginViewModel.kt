package com.projectdelta.zoro.ui.login

import androidx.lifecycle.ViewModel
import com.projectdelta.zoro.data.model.User
import com.projectdelta.zoro.data.preferences.PreferencesManager
import com.projectdelta.zoro.data.preferences.UserPreferences
import com.projectdelta.zoro.data.repository.UserRepository
import com.projectdelta.zoro.util.Constants
import com.projectdelta.zoro.util.system.lang.launchIO
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val userRepository: UserRepository,
    val preferencesManager: PreferencesManager
) : ViewModel() {

    inline fun requestNewUser(
        userName: String,
        crossinline onSuccess: (User) -> Unit,
        crossinline onFailure: () -> Unit,
    ) {
        launchIO {
            val user = User(
                id = Constants.generateUniqueUserId(),
                name = userName,
                connections = listOf()
            )
            val status = userRepository.addUser(user)
            if (status)
                onSuccess(user)
            else
                onFailure()
        }
    }

    inline fun updateUserPreferences(
        userPreferences: UserPreferences,
        crossinline onSuccess: () -> Unit
    ) = launchIO {
        preferencesManager.updateUserId(userPreferences.userId)
        preferencesManager.updateFirstLogin(userPreferences.firstLogin)
        preferencesManager.updateFirstLoginDate(userPreferences.firstLoginTime)
        preferencesManager.updateUserName(userPreferences.userName)
    }.invokeOnCompletion { error: Throwable? ->
        if (error == null)
            onSuccess()
    }

    inline fun userExists(
        userId: String,
        crossinline onSuccess: () -> Unit,
        crossinline onFailure: () -> Unit
    ) = launchIO {
        val userExists = userRepository.getUserById(userId)
        if (userExists != null)
            onSuccess()
        else
            onFailure()
    }

}