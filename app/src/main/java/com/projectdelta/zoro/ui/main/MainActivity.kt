/*
 * Copyright (c) 2022. Anshul Saraf
 */

package com.projectdelta.zoro.ui.main

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.projectdelta.zoro.R
import com.projectdelta.zoro.databinding.ActivityMainBinding
import com.projectdelta.zoro.ui.base.BaseViewBindingActivity
import com.projectdelta.zoro.ui.web.WebViewActivity
import com.projectdelta.zoro.util.Constants.ALERT_NOTIFICATION_DURATION
import com.projectdelta.zoro.util.NotFound
import com.projectdelta.zoro.util.system.lang.collectLatestLifecycleFlow
import com.projectdelta.zoro.util.system.lang.getResourceColor
import com.projectdelta.zoro.util.system.lang.getValueBlockedOrNull
import com.projectdelta.zoro.util.system.lang.slideDown
import com.projectdelta.zoro.util.system.lang.slideUp
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

    private fun registerObservers() {

        val userPreferences = preferenceManager.preferenceFlow.getValueBlockedOrNull()

        connectivityManager.isNetworkAvailable.observe(this) { isOnline ->
            when (isOnline) {
                true -> {
                    viewModel.registerClient(userPreferences?.userId!!)
                }
                false -> {
                    viewModel.unregisterClient()
                }
            }
        }

        collectLatestLifecycleFlow(viewModel.newMessage) {
            if (it != null)
                Alerter.create(this@MainActivity)
                    .setTitle("New Message from a friend.")// TODO( Fetch SenderName form server to display here )
                    .setText(it.data!!)
                    .setDuration(ALERT_NOTIFICATION_DURATION)
                    .setBackgroundColorInt(getResourceColor(R.attr.colorAccent))
                    .show()
        }

        collectLatestLifecycleFlow(viewModel.bottomNavVisibility) { integer ->
            when (integer) {
                View.GONE -> binding.bottomPanel.slideDown {
                    binding.bottomPanel.visibility = integer
                }
                View.VISIBLE -> binding.bottomPanel.slideUp {
                    binding.bottomPanel.visibility = integer
                }
                else -> throw NotFound.TheFuckHappened("Only GONE & VISIBLE state supported!")
            }
        }
    }

    private fun setupNavController() {
        val navController = findNavController(R.id.nav_host_fragment)
        binding.bottomPanel.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination: NavDestination, _ ->
            if (destination.id in listOf(R.id.chatFragment, R.id.settingFragment)) {
                viewModel.hideBottomNav()
            } else {
                viewModel.showBottomNav()
            }
        }
    }

    private fun initUI() {

    }

    fun launchWebView(url: String, title: String?) {
        WebViewActivity.newIntent(this, url, null, title).also {
            startActivity(it)
        }
    }

    override fun onDestroy() {
        viewModel.unregisterClient()
        super.onDestroy()
    }

}
