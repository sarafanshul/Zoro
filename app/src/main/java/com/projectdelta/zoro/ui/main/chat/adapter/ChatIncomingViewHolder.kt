package com.projectdelta.zoro.ui.main.chat.adapter

import android.view.View
import com.projectdelta.zoro.data.model.Message
import com.projectdelta.zoro.databinding.ItemChatIncomingBinding
import com.projectdelta.zoro.util.system.lang.chop
import java.util.*

class ChatIncomingViewHolder(
    private val binding: ItemChatIncomingBinding
) : ChatViewHolder<ItemChatIncomingBinding>(binding) {

    override fun bind(
        message: Message ,
        nextIsSame : Boolean ,
        onClickCallback: (m : Message ,c : Companion.ClickType) -> Unit
    ) {
        with(binding){
            tvChat.text = (" ".repeat(8) + message.data + " ".repeat(4)).chop(30)
            tvTime.text = formatter.format(Date(message.time ?: 0))

            root.setOnLongClickListener {
                onClickCallback( message , Companion.ClickType.LONG )
                true
            }

            root.setOnClickListener {
                onClickCallback(message , Companion.ClickType.SHORT)

                if( binding.tvTime.visibility == View.VISIBLE )
                    binding.tvTime.visibility = View.GONE
                else
                    binding.tvTime.visibility = View.VISIBLE
            }
        }
    }
}