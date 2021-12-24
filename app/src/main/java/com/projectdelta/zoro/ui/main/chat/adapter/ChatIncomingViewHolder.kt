package com.projectdelta.zoro.ui.main.chat.adapter

import com.projectdelta.zoro.data.model.Message
import com.projectdelta.zoro.databinding.ItemChatIncomingBinding

class ChatIncomingViewHolder(
    private val binding: ItemChatIncomingBinding
) : ChatViewHolder<ItemChatIncomingBinding>(binding) {

    override fun bind(
        message: Message,
        onClickCallback: (m: Message, c: Companion.ClickType) -> Unit
    ) {
        with(binding){
            tvChat.text = message.data

            root.setOnLongClickListener {
                onClickCallback( message , Companion.ClickType.LONG )
                true
            }
        }
    }
}