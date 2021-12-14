package com.projectdelta.zoro.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.projectdelta.zoro.data.preferences.UserPreferences
import com.projectdelta.zoro.databinding.ActivityMainBinding
import com.projectdelta.zoro.ui.base.BaseViewBindingActivity
import com.projectdelta.zoro.util.Constants.generateUniqueUserId
import com.projectdelta.zoro.util.networking.NetworkingConstants.getAvatarURIByUserId
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class MainActivity : BaseViewBindingActivity<ActivityMainBinding>() {

    private val _data : MutableLiveData<UserPreferences> = MutableLiveData()
    private val data : LiveData<UserPreferences> = _data

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        registerObservers()

        initUI()

    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun registerObservers(){
        connectivityManager.isNetworkAvailable.observe(this){ isOnline ->
            when(isOnline){
                true -> {
                    viewModel.registerClient()
                }
                false -> {
                    viewModel.unregisterClient()
                }
            }
        }

        lifecycleScope.launch {
            preferenceManager.preferenceFlow.collectLatest {
                _data.postValue(it)
            }
        }

        data.observe(this){
            Glide.with(this)
                .load(getAvatarURIByUserId(it.userId))
                .into(binding.test)
        }

        viewModel.data.observe(this){
            binding.msg.text = "${it?.data} : ${ SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS").format(Date(it?.time!!)) }"
        }
    }

    private fun initUI(){
        binding.tap.setOnClickListener{
            viewModel.sendData(
                binding.et.text.toString()
            )
        }
    }

    override fun onDestroy() {
        viewModel.unregisterClient()
        super.onDestroy()
    }

}