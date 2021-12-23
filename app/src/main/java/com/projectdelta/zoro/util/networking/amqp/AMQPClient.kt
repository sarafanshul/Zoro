package com.projectdelta.zoro.util.networking.amqp

import com.projectdelta.zoro.data.model.Message

interface AMQPClient {

    fun registerChannel()

    fun unregisterChannel()

    fun consumeMessage(queue: String, doSomething: (m: Message?) -> Unit)
}