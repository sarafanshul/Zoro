/*
 * Copyright (c) 2022. Anshul Saraf
 */

package com.projectdelta.zoro.util.networking.amqp

import com.projectdelta.zoro.data.model.Message

interface AMQPClient {

    fun setupConnection(host: String, port: Int, uName: String, password: String)

    fun registerChannel()

    fun unregisterChannel()

    fun consumeMessage(queue: String, doSomething: (m: Message?) -> Unit)
}
