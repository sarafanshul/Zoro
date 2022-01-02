package com.projectdelta.zoro.ui.main.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.projectdelta.zoro.databinding.FragmentSettingBinding
import com.projectdelta.zoro.ui.base.BaseViewBindingFragment
import com.projectdelta.zoro.util.Constants
import com.projectdelta.zoro.util.system.lang.biometricStatus
import com.projectdelta.zoro.util.system.lang.getValueBlockedOrNull
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : BaseViewBindingFragment<FragmentSettingBinding>() {

    private val viewModel : SettingViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _binding = FragmentSettingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
    }

    private fun initUI() {
        binding.biometrics.isChecked = viewModel.userPreferences
            .getValueBlockedOrNull()?.biometricEnabled!!

        binding.biometrics.isEnabled = when (requireActivity().biometricStatus) {
            Constants.BiometricStatus.STATUS_SUCCESS -> true
            else -> false
        }

        binding.biometrics.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateBiometricSetting(isChecked)
        }
    }

}