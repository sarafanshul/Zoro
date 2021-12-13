package com.projectdelta.zoro.data.model

import java.io.Serializable

data class MessageData(
    val senderId : String? = null ,
    val receiverId : String? = null,
    val data : String? = null,
    var time : Long? = null
) : Serializable
