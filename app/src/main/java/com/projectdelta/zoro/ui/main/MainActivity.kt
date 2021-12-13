package com.projectdelta.zoro.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.projectdelta.zoro.data.preferences.UserPreferences
import com.projectdelta.zoro.databinding.ActivityMainBinding
import com.projectdelta.zoro.ui.base.BaseViewBindingActivity
import com.projectdelta.zoro.util.Constants.generateUniqueUserId
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : BaseViewBindingActivity<ActivityMainBinding>() {

    private val _data : MutableLiveData<UserPreferences> = MutableLiveData()
    private val data : LiveData<UserPreferences> = _data

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        lifecycleScope.launch {
            preferenceManager.preferenceFlow.collectLatest {
                _data.postValue(it)
            }
        }

        data.observe(this){
            binding.test.text = "id : ${it.userId}\nfirst : ${it.firstLogin}"
        }

        binding.tap.setOnClickListener{
            lifecycleScope.launch{
                preferenceManager.updateUserId( generateUniqueUserId() )
            }
        }

    }
}