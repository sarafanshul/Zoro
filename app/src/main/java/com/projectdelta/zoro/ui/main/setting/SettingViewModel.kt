package com.projectdelta.zoro.ui.main.setting

import androidx.lifecycle.ViewModel
import com.projectdelta.zoro.data.preferences.PreferencesManager
import com.projectdelta.zoro.data.preferences.UserPreferences
import com.projectdelta.zoro.util.system.lang.launchIO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    val userPreferences : Flow<UserPreferences>
        get() = preferencesManager.preferenceFlow

    fun updateBiometricSetting( x : Boolean ) = launchIO {
        preferencesManager.updateBiometricStatus(x)
    }
}