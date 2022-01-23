/*
 * Copyright (c) 2022. Anshul Saraf
 */

package com.projectdelta.zoro.ui.main.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.projectdelta.zoro.data.model.User
import com.projectdelta.zoro.databinding.ItemHomeBinding
import com.projectdelta.zoro.util.callback.UserDiffUtilCallback
import com.projectdelta.zoro.util.system.lang.chop
import com.projectdelta.zoro.util.system.lang.isOk
import com.projectdelta.zoro.util.system.lang.loadUserProfileImage

class HomeRecyclerViewAdapter(
    private val onClickCallback: (User) -> Unit
) : ListAdapter<User, HomeRecyclerViewAdapter.ViewHolder>(UserDiffUtilCallback) {

    inner class ViewHolder(
        private val binding: ItemHomeBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User, onClickCallback: (User) -> Unit) {
            with(binding) {

                root.setOnClickListener {
                    onClickCallback(user)
                }

                loadUserProfileImage(ivUser, user.id!!)

                tvTitle.text = user.name
                tvTitle.isSelected = true
                tvSubTitle.text = if (user.lastMessage.isOk()) user.lastMessage.chop(25) else "..."
                if (user.messagesCount > 0) {
                    tvBadge.visibility = View.VISIBLE
                    tvBadge.text =
                        if (user.messagesCount > 9) "9+" else user.messagesCount.toString()
                } else {
                    tvBadge.visibility = View.GONE
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onClickCallback)
    }
}
