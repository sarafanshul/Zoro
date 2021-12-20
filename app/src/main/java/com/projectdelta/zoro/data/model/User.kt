package com.projectdelta.zoro.data.model

import java.io.Serializable

data class User(
    val id : String? = null,
    val name : String? = null,
    val connections : List<String>? = null,
    var messagesCount : Int = 0,
    var lastMessage : String = "",
) : Serializable
