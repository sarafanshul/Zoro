package com.projectdelta.zoro.ui.main.user

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.github.sumimakito.awesomeqr.AwesomeQrRenderer
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.integration.android.IntentIntegrator
import com.projectdelta.zoro.R
import com.projectdelta.zoro.data.preferences.UserPreferences
import com.projectdelta.zoro.databinding.FragmentUserBinding
import com.projectdelta.zoro.ui.base.BaseActivity
import com.projectdelta.zoro.ui.base.BaseViewBindingFragment
import com.projectdelta.zoro.util.image.QRGenerator.generateRenderOptions
import com.projectdelta.zoro.util.system.lang.getValueBlockedOrNull
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class UserFragment : BaseViewBindingFragment<FragmentUserBinding>() {

    private val viewModel: UserViewModel by viewModels()

    private var userPreferences : UserPreferences? = null

    @SuppressLint("SimpleDateFormat")
    private val formatter = SimpleDateFormat("dd MMMM yy")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userPreferences = (requireActivity() as BaseActivity)
                .preferenceManager.preferenceFlow.getValueBlockedOrNull()
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
    @Suppress("DEPRECATION") // FIXME
    private fun initUI() {

        binding.userName.text = userPreferences?.userName
        binding.userDate.text = "Since ${formatter.format(Date(userPreferences?.firstLoginTime ?: 0L))}"

        val opt = BitmapFactory.Options()
        opt.inMutable = true
        val renderOption = generateRenderOptions(
            content = userPreferences?.userId!!,
            backgroundImage = BitmapFactory
                .decodeResource(resources ,R.drawable.png_onepiece_background ,opt)
        )

        AwesomeQrRenderer.renderAsync(renderOption,
            resultCallback@{ result ->
                if (result.bitmap != null) {
                    runBlocking(Dispatchers.Main) {
                        binding.ivQr.setImageBitmap(result.bitmap)
                    }
                }
                else {
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

    @Suppress("DEPRECATION") // FIXME
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode ,resultCode ,data)
        if( result?.contents != null ){
            Snackbar.make(binding.root ,result.contents ,Snackbar.LENGTH_LONG).show()
        }else {
            Snackbar.make(binding.root ,"Scan error!" ,Snackbar.LENGTH_LONG).show()
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun registerObservers() {
//        collectLatestLifecycleFlow(viewModel.userPreferences) {
//            loadUserProfileImage(binding.ivUser ,it.userId)
//        }
    }

    override fun onDestroy() {
        userPreferences = null
        super.onDestroy()
    }

}