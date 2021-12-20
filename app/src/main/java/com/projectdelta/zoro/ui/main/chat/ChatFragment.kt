package com.projectdelta.zoro.ui.main.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.projectdelta.zoro.R
import com.projectdelta.zoro.data.model.User
import com.projectdelta.zoro.databinding.FragmentChatBinding
import com.projectdelta.zoro.ui.base.BaseViewBindingFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ChatFragment : BaseViewBindingFragment<FragmentChatBinding>() {

    lateinit var receiver : User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args : ChatFragmentArgs by navArgs()
        receiver = args.receiver
    }

    override fun onCreateView(inflater: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _binding = FragmentChatBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d(receiver.toString())
    }
}