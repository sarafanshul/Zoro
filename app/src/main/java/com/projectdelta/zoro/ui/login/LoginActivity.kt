/*
 * Copyright (c) 2022. Anshul Saraf
 */

package com.projectdelta.zoro.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.biometric.BiometricPrompt
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.work.WorkManager
import com.projectdelta.zoro.R
import com.projectdelta.zoro.data.preferences.UserPreferences
import com.projectdelta.zoro.databinding.ActivityLoginBinding
import com.projectdelta.zoro.ui.base.BaseViewBindingActivity
import com.projectdelta.zoro.ui.main.MainActivity
import com.projectdelta.zoro.util.Constants
import com.projectdelta.zoro.util.Constants.BiometricStatus
import com.projectdelta.zoro.util.system.lang.biometricStatus
import com.projectdelta.zoro.util.system.lang.getBiometricPrompt
import com.projectdelta.zoro.util.system.lang.getResourceColor
import com.projectdelta.zoro.util.system.lang.getValueBlockedOrNull
import com.projectdelta.zoro.util.system.lang.isOk
import com.projectdelta.zoro.util.system.lang.toEditable
import com.projectdelta.zoro.util.work.UpdateDatabaseWorker
import com.tapadoo.alerter.Alerter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseViewBindingActivity<ActivityLoginBinding>() {

    companion object{
        private const val CANCEL_BIOMETRICS = "Cancel"
    }

    private lateinit var biometricPrompt: BiometricPrompt

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        _binding = ActivityLoginBinding.inflate(layoutInflater)
        viewModel

        setContentView(binding.root)

        setupWorker()

        initUI()
    }

    private fun setupWorker() {
        WorkManager.getInstance(applicationContext)
            .enqueue(UpdateDatabaseWorker.workerRequest)
    }

    private fun initUI() {

        val userPreferences = preferenceManager.preferenceFlow.getValueBlockedOrNull()

        if (userPreferences?.firstLogin == true) {
            binding.userName.setOnEditorActionListener { v, actionId, _ ->
                var handled = false
                val userName = v.text.toString()
                if (actionId == EditorInfo.IME_ACTION_DONE && userName.isOk()) {
                    binding.progressBar.isVisible = true
                    viewModel.requestNewUser(userName, onSuccess@{
                        val userPref = UserPreferences(
                            userId = it.id!!,
                            userName = it.name!!,
                            firstLogin = false,
                            firstLoginTime = System.currentTimeMillis(),
                            biometricEnabled = false
                        )
                        viewModel.updateUserPreferences(userPref) onPrefSaved@{
                            launchMainActivity()
                        }
                    }, onFailure@{
                        binding.progressBar.isVisible = false
                        showError("Some error occurred.", "Please try again!")
                    })
                    handled = true
                }
                handled
            }
        } else {
            binding.userName.text = userPreferences?.userName?.toEditable()
            binding.userName.isEnabled = false
            binding.progressBar.isVisible = true

            if (userPreferences?.biometricEnabled == true &&
                biometricStatus == BiometricStatus.STATUS_SUCCESS
            ) launchBiometricPrompt()
            else
                launchMainActivity()
        }
    }

    private fun launchBiometricPrompt() {
        biometricPrompt = getBiometricPrompt(
            onSuccess@{
                launchMainActivity()
            },
            onError@{ error ->
                if( error == CANCEL_BIOMETRICS )
                    finish()
                else
                    showError(error, "Please try again.")
            }
        )
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Secure Login")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText(CANCEL_BIOMETRICS)
            .build()

        biometricPrompt.authenticate(promptInfo)

    }

    private fun launchMainActivity() {
        val userPreferences = preferenceManager.preferenceFlow.getValueBlockedOrNull()
        viewModel.userExists(userPreferences?.userId!!, onSuccess@{
            Intent(this, MainActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }, onFailure@{
            showError("Some error occurred...", "While connecting to server!")
        }
        )
    }

    private fun showError(title: String, text: String) {
        Alerter.create(this@LoginActivity)
            .setTitle(title)
            .setText(text)
            .setDuration(Constants.ALERT_NOTIFICATION_DURATION)
            .setBackgroundColorInt(getResourceColor(R.attr.colorError))
            .show()
    }

}
