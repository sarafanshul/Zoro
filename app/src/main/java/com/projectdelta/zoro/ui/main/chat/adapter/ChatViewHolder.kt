package com.projectdelta.zoro.ui.main.chat.adapter

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.projectdelta.zoro.data.model.Message

abstract class ChatViewHolder<VB : ViewBinding>(
    binding : VB
) : RecyclerView.ViewHolder(binding.root) {

    companion object{
        enum class ClickType{
            SHORT , LONG
        }
    }

    abstract fun bind( message: Message ,onClickCallback: (m : Message ,c : ClickType) -> Unit)

}