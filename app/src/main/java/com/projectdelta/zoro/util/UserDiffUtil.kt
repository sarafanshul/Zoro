package com.projectdelta.zoro.util

import androidx.recyclerview.widget.DiffUtil
import com.projectdelta.zoro.data.model.User
import timber.log.Timber

object UserDiffUtil : DiffUtil.ItemCallback<User>(){
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        Timber.d("areContentsTheSame : ${oldItem.name} :${oldItem.messagesCount} , ${newItem.name} :${newItem.messagesCount}")
        return oldItem.name == newItem.name &&
                oldItem.messagesCount == newItem.messagesCount
    }
}