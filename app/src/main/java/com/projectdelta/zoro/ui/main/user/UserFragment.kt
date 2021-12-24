package com.projectdelta.zoro.ui.main.user

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.github.sumimakito.awesomeqr.AwesomeQrRenderer
import com.projectdelta.zoro.R
import com.projectdelta.zoro.databinding.FragmentUserBinding
import com.projectdelta.zoro.ui.base.BaseViewBindingFragment
import com.projectdelta.zoro.util.image.QRGenerator.generateRenderOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import timber.log.Timber

@AndroidEntryPoint
class UserFragment : BaseViewBindingFragment<FragmentUserBinding>() {

    private val viewModel: UserViewModel by viewModels()

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

    private fun initUI() {
        val opt = BitmapFactory.Options()
        opt.inMutable = true
        val renderOption = generateRenderOptions(
            content = "test",
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
    }

    private fun registerObservers() {
//        collectLatestLifecycleFlow(viewModel.userPreferences) {
//            loadUserProfileImage(binding.ivUser ,it.userId)
//        }
    }

}