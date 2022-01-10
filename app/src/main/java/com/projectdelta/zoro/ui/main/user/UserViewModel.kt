package com.projectdelta.zoro.ui.main.user

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.github.sumimakito.awesomeqr.option.RenderOption
import com.projectdelta.zoro.data.model.ConnectionData
import com.projectdelta.zoro.data.preferences.PreferencesManager
import com.projectdelta.zoro.data.preferences.UserPreferences
import com.projectdelta.zoro.data.repository.UserRepository
import com.projectdelta.zoro.util.image.QRGenerator
import com.projectdelta.zoro.util.system.lang.getValueBlockedOrNull
import com.projectdelta.zoro.util.system.lang.launchIO
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val userRepository: UserRepository,
) : ViewModel() {

    val userPreferences: UserPreferences?
        get() = preferencesManager.preferenceFlow.getValueBlockedOrNull()

    private var _renderOption: RenderOption? = null
    fun getRenderOption(bitmap: Bitmap): RenderOption {
        if (_renderOption == null) {
            _renderOption = QRGenerator.generateRenderOptions(
                content = userPreferences?.userId!!,
                backgroundImage = bitmap
            )
        }
        return _renderOption!!
    }

    fun connectUser(userId: String, doAfter: suspend () -> Unit) {
        launchIO {
            val x = userRepository.connectUser(
                ConnectionData(
                    senderUser = userPreferences?.userId,
                    receiverUser = userId
                )
            )
            if (x)
                doAfter()
        }
    }
}