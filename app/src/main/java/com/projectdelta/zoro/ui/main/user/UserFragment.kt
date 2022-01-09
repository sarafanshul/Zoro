@file:Suppress("DEPRECATION") // FIXME - IntentIntegrator/onActivityResult
package com.projectdelta.zoro.ui.main.user

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.github.sumimakito.awesomeqr.AwesomeQrRenderer
import com.google.zxing.integration.android.IntentIntegrator
import com.projectdelta.zoro.R
import com.projectdelta.zoro.databinding.FragmentUserBinding
import com.projectdelta.zoro.ui.base.BaseViewBindingFragment
import com.projectdelta.zoro.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class UserFragment : BaseViewBindingFragment<FragmentUserBinding>() {

    private val viewModel: UserViewModel by viewModels()
    private val activityViewModel: MainViewModel by activityViewModels()

    private var displayBitmap : Bitmap? = null

    @SuppressLint("SimpleDateFormat")
    private val formatter = SimpleDateFormat("dd MMMM yy")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val opt = BitmapFactory.Options()
        opt.inMutable = true

        displayBitmap = BitmapFactory
            .decodeResource(resources, R.drawable.png_onepiece_background, opt)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        c: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerObservers()

        initUI()

    }

    @SuppressLint("SetTextI18n")
    private fun initUI() {

        binding.userName.text = viewModel.userPreferences?.userName
        binding.userDate.text =
            "Since ${formatter.format(Date(viewModel.userPreferences?.firstLoginTime ?: 0L))}"

        val opt = BitmapFactory.Options()
        opt.inMutable = true

        AwesomeQrRenderer.renderAsync(viewModel.getRenderOption(displayBitmap!!),
            resultCallback@{ result ->
                if (result.bitmap != null) {
                    runBlocking(Dispatchers.Main) {
                        binding.ivQr.setImageBitmap(result.bitmap)
                    }
                } else {
                    Timber.e("Opps! Something gone wrong.. :((")
                }
            },
            errorCallback@{ error ->
                Timber.e(error)
            }
        )

        binding.scan.setOnClickListener {
            val integrator = IntentIntegrator.forSupportFragment(this)
            integrator.setOrientationLocked(true)
            integrator.setPrompt("Scan Connection's QR")
            integrator.setBeepEnabled(false)
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)

            integrator.initiateScan()
        }

    }

    private fun registerObservers() {}

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result?.contents != null) {
            viewModel.connectUser(result.contents) {
                activityViewModel.refreshConnectionList
                    .emit(MainViewModel.Companion.RefreshType.CONNECTION_LIST)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onDestroy() {
        displayBitmap = null
        super.onDestroy()
    }

}