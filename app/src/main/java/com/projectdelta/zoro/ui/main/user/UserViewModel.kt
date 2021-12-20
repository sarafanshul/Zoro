package com.projectdelta.zoro.ui.main.user

import androidx.lifecycle.ViewModel
import com.projectdelta.zoro.data.preferences.PreferencesManager
import com.projectdelta.zoro.data.preferences.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    val userPreferences : Flow<UserPreferences>
        get() = preferencesManager.preferenceFlow
}