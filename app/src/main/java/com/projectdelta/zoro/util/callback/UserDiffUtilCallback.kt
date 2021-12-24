package com.projectdelta.zoro.util.callback

import androidx.recyclerview.widget.DiffUtil
import com.projectdelta.zoro.data.model.User

object UserDiffUtilCallback : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.name == newItem.name &&
                oldItem.messagesCount == newItem.messagesCount
    }
}