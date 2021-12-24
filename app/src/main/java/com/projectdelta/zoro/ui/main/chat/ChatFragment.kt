package com.projectdelta.zoro.ui.main.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.projectdelta.zoro.data.model.Message
import com.projectdelta.zoro.data.model.User
import com.projectdelta.zoro.databinding.FragmentChatBinding
import com.projectdelta.zoro.ui.base.BaseViewBindingFragment
import com.projectdelta.zoro.ui.main.MainViewModel
import com.projectdelta.zoro.ui.main.chat.adapter.ChatRecyclerAdapter
import com.projectdelta.zoro.util.system.lang.*
import com.projectdelta.zoro.util.widget.SpaceItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ChatFragment : BaseViewBindingFragment<FragmentChatBinding>() {

    private lateinit var receiver: User

    private val viewModel : ChatViewModel by viewModels()
    private val activityViewModel : MainViewModel by activityViewModels()

    private var adapter : ChatRecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args: ChatFragmentArgs by navArgs()
        receiver = args.receiver

        viewModel.getReceiverUserData(receiver.id!!)

        activityViewModel.currentChatReceiver = receiver.id!!

        adapter = ChatRecyclerAdapter{ m ,c ->
            Timber.d("${m.data} : ${c.name}")
        }

    }

    override fun onCreateView(inflater: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _binding = FragmentChatBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerObservers()

        initUI()
    }

    private fun initUI() {
        binding.rvChat.adapter = adapter

        binding.rvChat.addItemDecoration(SpaceItemDecoration(8))

        binding.tvSend.setOnEditorActionListener { v, actionId, _ ->
            var handled = false
            if( actionId == EditorInfo.IME_ACTION_SEND ){
                sendMessage(v.text.toString())
                handled = true
            }
            handled
        }

        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        loadUserProfileImage(binding.ivUser ,receiver.id!!)

        binding.tvUser.text = receiver.name?.chop(25)

    }

    private fun sendMessage( text : String? ){
        if( text.isOk() ) {
            val message = Message(
                receiverId = receiver.id,
                senderId = "7",
                data = text,
                time = System.currentTimeMillis(),
                type = Message.Companion.MessageType.OUTGOING
            )
            viewModel.sendMessage(message)
            binding.tvSend.text = "".toEditable()
        }
    }

    private fun registerObservers(){
        collectLatestLifecycleFlow(viewModel.messages){
            adapter?.submitList(it)
        }
    }

    override fun onDestroyView() {
        binding.rvChat.adapter = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        adapter = null
        activityViewModel.currentChatReceiver = ""
        activityViewModel.setMessagesSeen(receiver.id!!)
        super.onDestroy()
    }
}