package com.projectdelta.zoro.ui.main.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.projectdelta.zoro.data.preferences.UserPreferences
import com.projectdelta.zoro.databinding.FragmentUserBinding
import com.projectdelta.zoro.ui.base.BaseActivity
import com.projectdelta.zoro.ui.base.BaseViewBindingFragment
import com.projectdelta.zoro.util.networking.NetworkingConstants
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class UserFragment : BaseViewBindingFragment<FragmentUserBinding>() {

    private val data : MutableLiveData<UserPreferences?> = MutableLiveData()

    override fun onCreateView(inflater: LayoutInflater, c: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentUserBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            (requireActivity() as BaseActivity).preferenceManager.preferenceFlow.collectLatest {
                data.postValue(it)
            }
        }

        data.observe(viewLifecycleOwner){
            Glide
                .with(binding.root.context)
                .load(NetworkingConstants.getAvatarURIByUserId(it?.userId!!))
                .into(binding.ivUser)
        }
    }

}