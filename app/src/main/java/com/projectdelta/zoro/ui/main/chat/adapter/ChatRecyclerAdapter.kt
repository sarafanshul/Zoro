/*
 * Copyright (c) 2022. Anshul Saraf
 */

package com.projectdelta.zoro.ui.main.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.projectdelta.zoro.data.model.Message
import com.projectdelta.zoro.databinding.ItemChatIncomingBinding
import com.projectdelta.zoro.databinding.ItemChatOutgoingBinding
import com.projectdelta.zoro.ui.main.chat.adapter.ChatViewHolder.Companion.ClickType
import com.projectdelta.zoro.util.NotFound
import com.projectdelta.zoro.util.callback.MessageDiffUtilCallback

class ChatRecyclerAdapter(
    private val onClickCallback: (m: Message, c: ClickType) -> Unit
) : ListAdapter<Message, ChatViewHolder<*>>(MessageDiffUtilCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder<*> {
        return when (viewType) {
            Message.Companion.MessageType.INCOMING.ordinal -> {
                val binding = ItemChatIncomingBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
                ChatIncomingViewHolder(binding)
            }
            Message.Companion.MessageType.OUTGOING.ordinal -> {
                val binding = ItemChatOutgoingBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
                ChatOutgoingViewHolder(binding)
            }
            else ->
                throw NotFound.TheFuckHappened("Not a valid item type $viewType")
        }
    }

    override fun onBindViewHolder(holder: ChatViewHolder<*>, position: Int) {
        val nextIsSame: Boolean =
            if (position + 1 < itemCount)
                getItem(position + 1).type == getItem(position).type
            else false
        holder.bind(getItem(position), nextIsSame, onClickCallback)
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type.ordinal
    }
}
