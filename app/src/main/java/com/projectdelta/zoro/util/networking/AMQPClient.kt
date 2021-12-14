package com.projectdelta.zoro.util.networking

import com.projectdelta.zoro.data.model.MessageData

interface AMQPClient {

    fun registerChannel()

    fun unregisterChannel()

    fun consumeMessage( queue : String , doSomething : (m : MessageData?) -> Unit )
}