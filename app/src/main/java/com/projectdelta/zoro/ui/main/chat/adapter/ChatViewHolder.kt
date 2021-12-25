package com.projectdelta.zoro.ui.main.chat.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.projectdelta.zoro.data.model.Message
import java.text.SimpleDateFormat

abstract class ChatViewHolder<VB : ViewBinding>(
    binding : VB
) : RecyclerView.ViewHolder(binding.root) {

    companion object{
        enum class ClickType{
            SHORT , LONG
        }
    }

    @SuppressLint("SimpleDateFormat")
    protected val formatter = SimpleDateFormat("HH:mm")

    abstract fun bind( message: Message ,onClickCallback: (m : Message ,c : ClickType) -> Unit)

}