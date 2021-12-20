package com.projectdelta.zoro.ui.main.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.projectdelta.zoro.databinding.FragmentUserBinding
import com.projectdelta.zoro.ui.base.BaseViewBindingFragment
import com.projectdelta.zoro.util.networking.NetworkingConstants
import com.projectdelta.zoro.util.system.lang.collectLatestLifecycleFlow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserFragment : BaseViewBindingFragment<FragmentUserBinding>() {

    private val viewModel : UserViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, c: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentUserBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerObservers()

    }

    private fun registerObservers(){
        collectLatestLifecycleFlow(viewModel.userPreferences){
            Glide
                .with(binding.root.context)
                .load(NetworkingConstants.getAvatarURIByUserId(it.userId))
                .into(binding.ivUser)
        }
    }

}