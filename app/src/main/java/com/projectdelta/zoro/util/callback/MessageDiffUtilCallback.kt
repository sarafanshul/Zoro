/*
 * Copyright (c) 2022. Anshul Saraf
 */

package com.projectdelta.zoro.util.callback

import androidx.recyclerview.widget.DiffUtil
import com.projectdelta.zoro.data.model.Message

object MessageDiffUtilCallback : DiffUtil.ItemCallback<Message>() {

    override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem == newItem
    }
}
