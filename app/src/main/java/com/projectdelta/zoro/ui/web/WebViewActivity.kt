/*
 * Copyright (c) 2022. Anshul Saraf
 */

package com.projectdelta.zoro.ui.web

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.graphics.Bitmap
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.Toast
import androidx.core.graphics.ColorUtils
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.projectdelta.zoro.BuildConfig
import com.projectdelta.zoro.R
import com.projectdelta.zoro.databinding.ActivityWebViewBinding
import com.projectdelta.zoro.ui.base.BaseViewBindingActivity
import com.projectdelta.zoro.util.system.lang.getResourceColor
import com.projectdelta.zoro.util.system.lang.openInBrowser
import com.projectdelta.zoro.util.system.lang.toast
import com.projectdelta.zoro.util.web.WebViewClientCompat
import com.projectdelta.zoro.util.web.WebViewUtil
import com.projectdelta.zoro.util.web.setDefaultSettings
import timber.log.Timber

class WebViewActivity : BaseViewBindingActivity<ActivityWebViewBinding>() {

    companion object {
        private const val URL_KEY = "URL_KEY"
        private const val SOURCE_KEY = "SOURCE_KEY"
        private const val TITLE_KEY = "TITLE_KEY"

        fun newIntent(
            context: Context,
            url: String,
            sourceId: Long? = null,
            title: String? = null
        ): Intent {
            return Intent(context, WebViewActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                putExtra(URL_KEY, url)
                putExtra(SOURCE_KEY, sourceId)
                putExtra(TITLE_KEY, title)
            }
        }
    }

    private var isRefreshing: Boolean = false

    private var bundle: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            _binding = ActivityWebViewBinding.inflate(layoutInflater)
            setContentView(binding.root)
        } catch (e: Throwable) {
            // Potentially throws errors like "Error inflating class android.webkit.WebView"
            toast("Error! , WebView required.", Toast.LENGTH_LONG)
            Timber.e(e)
            finish()
            return
        }

        title = intent.extras?.getString(TITLE_KEY)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            super.onBackPressed()
        }

        binding.swipeRefresh.isEnabled = false
        binding.swipeRefresh.setOnRefreshListener {
            refreshPage()
        }

        if (bundle == null) {
            binding.webview.setDefaultSettings()

            // Debug mode (chrome://inspect/#devices)
            if (BuildConfig.DEBUG && 0 != applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) {
                WebView.setWebContentsDebuggingEnabled(true)
            }

            binding.webview.webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    binding.progressBar.isVisible = true
                    binding.progressBar.progress = newProgress
                    if (newProgress == 100) {
                        binding.progressBar.isInvisible = true
                    }
                    super.onProgressChanged(view, newProgress)
                }
            }
        } else {
            binding.webview.restoreState(bundle!!)
        }
        if (bundle == null) {
            val url = intent.extras!!.getString(URL_KEY) ?: return

            val headers = mutableMapOf<String, String>()
            headers["X-Requested-With"] = WebViewUtil.REQUESTED_WITH

            supportActionBar?.subtitle = url

            binding.webview.webViewClient = object : WebViewClientCompat() {
                override fun shouldOverrideUrlCompat(view: WebView, url: String): Boolean {
                    view.loadUrl(url, headers)
                    return true
                }

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    invalidateOptionsMenu()
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    invalidateOptionsMenu()
                    title = view?.title
                    supportActionBar?.subtitle = url
                    binding.swipeRefresh.isEnabled = true
                    binding.swipeRefresh.isRefreshing = false

                    // Reset to top when page refreshes
                    if (isRefreshing) {
                        view?.scrollTo(0, 0)
                        isRefreshing = false
                    }
                }
            }

            binding.webview.loadUrl(url, headers)
            Timber.d("onCreate: LoadUrl : $url")
        }
    }

    @Suppress("UNNECESSARY_SAFE_CALL")
    override fun onDestroy() {
        // Binding sometimes isn't actually instantiated yet somehow
        _binding?.webview?.destroy()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.webview, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val backItem = menu?.findItem(R.id.action_web_back)
        val forwardItem = menu?.findItem(R.id.action_web_forward)
        backItem?.isEnabled = binding.webview.canGoBack()
        forwardItem?.isEnabled = binding.webview.canGoForward()

        val iconTintColor = getResourceColor(R.attr.colorOnPrimary)
        val translucentIconTintColor = ColorUtils.setAlphaComponent(iconTintColor, 127)
        backItem?.icon?.setTint(if (binding.webview.canGoBack()) iconTintColor else translucentIconTintColor)
        forwardItem?.icon?.setTint(if (binding.webview.canGoForward()) iconTintColor else translucentIconTintColor)

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_web_back -> binding.webview.goBack()
            R.id.action_web_forward -> binding.webview.goForward()
            R.id.action_web_refresh -> refreshPage()
            R.id.action_web_share -> shareWebpage()
            R.id.action_web_browser -> openInBrowser()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (binding.webview.canGoBack()) binding.webview.goBack()
        else super.onBackPressed()
    }

    private fun refreshPage() {
        binding.swipeRefresh.isRefreshing = true
        binding.webview.reload()
        isRefreshing = true
    }

    private fun shareWebpage() {
        try {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, binding.webview.url)
            }
            startActivity(Intent.createChooser(intent, getString(R.string.action_share)))
        } catch (e: Exception) {
            toast(e.message)
        }
    }

    private fun openInBrowser() {
        openInBrowser(binding.webview.url!!)
    }

}
