package com.projectdelta.zoro.ui.main.chat.adapter

import com.projectdelta.zoro.data.model.Message
import com.projectdelta.zoro.databinding.ItemChatIncomingBinding
import com.projectdelta.zoro.util.system.lang.chop
import java.util.*

class ChatIncomingViewHolder(
    private val binding: ItemChatIncomingBinding
) : ChatViewHolder<ItemChatIncomingBinding>(binding) {

    override fun bind(
        message: Message,
        onClickCallback: (m: Message, c: Companion.ClickType) -> Unit
    ) {
        with(binding){
            tvChat.text = (message.data + "         ").chop(30)
            tvTime.text = formatter.format(Date(message.time ?: 0))

            root.setOnLongClickListener {
                onClickCallback( message , Companion.ClickType.LONG )
                true
            }

            root.setOnClickListener {
                onClickCallback(message , Companion.ClickType.SHORT)
            }
        }
    }
}