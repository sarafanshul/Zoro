package com.projectdelta.zoro.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.projectdelta.zoro.R
import com.projectdelta.zoro.databinding.ActivityMainBinding
import com.projectdelta.zoro.ui.base.BaseViewBindingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseViewBindingActivity<ActivityMainBinding>() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        registerObservers()

        setupNavController()

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
    }

    private fun setupNavController(){
        val navController = findNavController(R.id.nav_host_fragment)
        binding.bottomPanel.setupWithNavController(navController)
    }

    private fun initUI(){

    }

    override fun onDestroy() {
        viewModel.unregisterClient()
        super.onDestroy()
    }

}