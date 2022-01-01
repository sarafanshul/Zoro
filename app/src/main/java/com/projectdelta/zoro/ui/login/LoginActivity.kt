package com.projectdelta.zoro.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.biometric.BiometricPrompt
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar
import com.projectdelta.zoro.data.preferences.UserPreferences
import com.projectdelta.zoro.databinding.ActivityLoginBinding
import com.projectdelta.zoro.ui.base.BaseViewBindingActivity
import com.projectdelta.zoro.ui.main.MainActivity
import com.projectdelta.zoro.util.Constants.BiometricStatus
import com.projectdelta.zoro.util.system.lang.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseViewBindingActivity<ActivityLoginBinding>() {

    private var biometricPrompt : BiometricPrompt? = null

    private val viewModel : LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityLoginBinding.inflate(layoutInflater)
        viewModel

        setContentView(binding.root)

        initUI()
    }

    private fun initUI(){

        val userPreferences = preferenceManager.preferenceFlow.getValueBlockedOrNull()

        if( userPreferences?.firstLogin == true ){
            binding.userName.setOnEditorActionListener { v, actionId, _ ->
                var handled = false
                val userName = v.text.toString()
                if( actionId == EditorInfo.IME_ACTION_DONE && userName.isOk() ){
                    binding.progressBar.isVisible = true
                    viewModel.requestNewUser(userName,onSuccess@{
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
                        Snackbar.make(binding.root ,"Some error occurred!" ,Snackbar.LENGTH_LONG)
                            .show()
                    })
                    handled = true
                }
                handled
            }
        }else{
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
        if( biometricPrompt != null )
            biometricPrompt = getBiometricPrompt(
                onSuccess@{
                    launchMainActivity()
                } ,
                onError@{
                    Snackbar.make(binding.root ,"Some error occurred , try again!" ,Snackbar.LENGTH_SHORT)
                        .show()
                }
            )
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account password")
            .build()

        biometricPrompt?.authenticate(promptInfo)

    }

    private fun launchMainActivity() =
        Intent(this ,MainActivity::class.java).also {
            startActivity(it)
            finish()
        }

    override fun onDestroy() {
        biometricPrompt = null
        super.onDestroy()
    }

}