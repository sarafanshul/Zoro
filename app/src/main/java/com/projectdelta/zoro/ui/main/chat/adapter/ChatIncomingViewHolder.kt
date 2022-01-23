/*
 * Copyright (c) 2022. Anshul Saraf
 */

package com.projectdelta.zoro.ui.main.chat.adapter

import android.view.View
import com.projectdelta.zoro.data.model.Message
import com.projectdelta.zoro.databinding.ItemChatIncomingBinding

class ChatIncomingViewHolder(
    private val binding: ItemChatIncomingBinding
) : ChatViewHolder<ItemChatIncomingBinding>(binding) {

    override fun bind(
        message: Message,
        nextIsSame: Boolean,
        onClickCallback: (m: Message, c: Companion.ClickType) -> Unit
    ) {
        with(binding) {
            tvChat.text = formatMessageText(message.data)
            tvTime.text = formatMessageDate(message.time)

            root.setOnLongClickListener {
                onClickCallback(message, Companion.ClickType.LONG)
                true
            }

            root.setOnClickListener {
                onClickCallback(message, Companion.ClickType.SHORT)

                if (binding.tvTime.visibility == View.VISIBLE)
                    binding.tvTime.visibility = View.GONE
                else
                    binding.tvTime.visibility = View.VISIBLE
            }
        }
    }
}
