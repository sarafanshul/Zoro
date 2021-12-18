package com.projectdelta.zoro.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.projectdelta.zoro.R
import com.projectdelta.zoro.databinding.ActivityMainBinding
import com.projectdelta.zoro.ui.base.BaseViewBindingActivity
import com.projectdelta.zoro.util.system.lang.chop
import com.projectdelta.zoro.util.system.lang.collectLatestLifecycleFlow
import com.projectdelta.zoro.util.system.lang.getResourceColor
import com.tapadoo.alerter.Alerter
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

        collectLatestLifecycleFlow(viewModel.newMessage){
            if( it != null )
                Alerter.create(this@MainActivity)
                    .setTitle("New Message from ${it.senderId?.chop(20)}")
                    .setText(it.data!!)
                    .setDuration(5000L)
                    .setBackgroundColorInt(getResourceColor(R.attr.colorAccent))
                    .show()
        }

        // TODO CHECK MAP IS REFERENCE OR COPY IN FLOW
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