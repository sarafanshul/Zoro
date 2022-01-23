/*
 * Copyright (c) 2022. Anshul Saraf
 */

package com.projectdelta.zoro.ui.main.chat.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.projectdelta.zoro.data.model.Message
import com.projectdelta.zoro.util.system.lang.chop
import java.text.SimpleDateFormat
import java.util.Date

abstract class ChatViewHolder<VB : ViewBinding>(
    binding: VB
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        enum class ClickType {
            SHORT, LONG
        }

        const val MAX_CHAT_TEXT_LENGTH = 500
    }

    @SuppressLint("SimpleDateFormat")
    private val formatter = SimpleDateFormat("HH:mm")

    abstract fun bind(
        message: Message,
        nextIsSame: Boolean,
        onClickCallback: (m: Message, c: ClickType) -> Unit
    )

    protected fun formatMessageText(message: String? ,length : Int = MAX_CHAT_TEXT_LENGTH) =
        " $message ".chop(length)

    protected fun formatMessageDate(timeInMillis: Long?): String =
        formatter.format(Date(timeInMillis ?: 0))

}
