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
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.bumptech.glide.Glide
import com.github.sumimakito.awesomeqr.AwesomeQrRenderer
import com.google.zxing.integration.android.IntentIntegrator
import com.projectdelta.zoro.R
import com.projectdelta.zoro.databinding.FragmentUserBinding
import com.projectdelta.zoro.databinding.LayoutQrBinding
import com.projectdelta.zoro.ui.base.BaseViewBindingFragment
import com.projectdelta.zoro.ui.main.MainViewModel
import com.projectdelta.zoro.util.networking.NetworkingConstants
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date

@AndroidEntryPoint
class UserFragment : BaseViewBindingFragment<FragmentUserBinding>() {

    private val viewModel: UserViewModel by viewModels()
    private val activityViewModel: MainViewModel by activityViewModels()

    @SuppressLint("SimpleDateFormat")
    private val formatter = SimpleDateFormat("dd MMMM yy")

    private var _qrViewBinding : LayoutQrBinding? = null
    private val qrViewBinding : LayoutQrBinding
        get() = _qrViewBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val opt = BitmapFactory.Options()
        opt.inMutable = true

        generateQR(
            BitmapFactory
                .decodeResource(
                    resources,
                    R.drawable.png_onepiece_background,
                    opt
                )
        )

    }

    override fun onCreateView(inflater: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _binding = FragmentUserBinding.inflate(inflater)

        _qrViewBinding = LayoutQrBinding.inflate(
            LayoutInflater.from(context) ,binding.root , true
        )

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
        binding.userDate.text = "Since ${formatter.format(Date(viewModel.userPreferences?.firstLoginTime ?: 0L))}"

        Glide.with(this)
            .load(NetworkingConstants.getAvatarURIByUserId(viewModel.userPreferences?.userId!!))
            .into(binding.userImage)

        binding.scan.setOnClickListener {
            launchScanner()
        }

        binding.qr.setOnClickListener {
            showQr()
        }

    }

    private fun registerObservers() {}

    private fun showQr() {
        qrViewBinding.imageQr.setImageBitmap(viewModel.qrBitmap)
        MaterialDialog(requireActivity()).show {
            cornerRadius(16f)
            customView(
                view = qrViewBinding.root ,
                dialogWrapContent = true ,
                noVerticalPadding = true
            )
        }.show()
    }

    private fun launchScanner() {
        val integrator = IntentIntegrator.forSupportFragment(this)
        integrator.setOrientationLocked(true)
        integrator.setPrompt("Scan Connection's QR")
        integrator.setBeepEnabled(false)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.initiateScan()
    }

    private fun generateQR(res: Bitmap) {
        AwesomeQrRenderer.renderAsync(viewModel.getRenderOption(res),
            resultCallback@{ result ->
                if (result.bitmap != null) {
                    viewModel.qrBitmap = result.bitmap
                } else { Timber.e("Error loading bitmap") }
            },
            errorCallback@{ error ->
                Timber.e(error)
            }
        )
    }

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

    override fun onDestroyView() {
        _qrViewBinding = null
        super.onDestroyView()
    }

}
