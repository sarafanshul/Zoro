package com.projectdelta.zoro.data.model

import java.io.Serializable

data class MessageData(
    val senderId : String? = null ,
    val receiverId : String? = null,
    val data : String? = null,
    val time : Long? = null,
    var seen : Boolean = false
) : Serializable
