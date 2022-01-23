/*
 * Copyright (c) 2022. Anshul Saraf
 */

package com.projectdelta.zoro.data.model

data class User(
    val id: String? = null,
    val name: String? = null,
    val connections: List<String>? = null,
    var messagesCount: Int = 0,
    var lastMessage: String = "",
) : BaseDataModel() {

    override fun copy() =
        User(id, name, connections, messagesCount, lastMessage)
}
