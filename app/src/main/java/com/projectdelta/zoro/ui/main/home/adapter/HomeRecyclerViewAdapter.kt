package com.projectdelta.zoro.ui.main.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.projectdelta.zoro.R
import com.projectdelta.zoro.data.model.User
import com.projectdelta.zoro.databinding.ItemHomeBinding
import com.projectdelta.zoro.util.UserDiffUtil
import com.projectdelta.zoro.util.networking.NetworkingConstants

class HomeRecyclerViewAdapter(
    private val onClickCallback : ( User ) -> Unit
) : ListAdapter<User ,HomeRecyclerViewAdapter.ViewHolder>(UserDiffUtil) {

    inner class ViewHolder(
        private val binding : ItemHomeBinding
    ) : RecyclerView.ViewHolder(binding.root){

        fun bind( user : User , onClickCallback : ( User ) -> Unit ){
            with(binding){
                val context = root.context

                root.setOnClickListener {
                    onClickCallback(user)
                }

                Glide
                    .with(context)
                    .load(NetworkingConstants.getAvatarURIByUserId(user.id!!))
                    .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA))
                    .into(ivUser)

                tvTitle.text = user.name
                tvTitle.isSelected = true
                tvSubTitle.setText(R.string.home)
                if(user.messages != null){
                    tvBadge.visibility = View.VISIBLE
                    tvBadge.text = if (user.messages!! > 9) "9+" else user.messages.toString()
                }else{
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
        holder.bind( getItem(position) , onClickCallback )
    }
}