/*
 * Copyright (c) 2022. Anshul Saraf
 */

package com.projectdelta.zoro.util.networking.amqp

import com.google.gson.Gson
import com.projectdelta.zoro.data.model.Message
import com.rabbitmq.client.AMQP
import com.rabbitmq.client.Channel
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import timber.log.Timber

class AMQPConsumer(
    channel: Channel,
    private val deserializer: Gson,
    private val onMessageReceive : (m : Message) -> Unit
) : DefaultConsumer(channel){

    override fun handleDelivery(
        tag: String,
        e: Envelope?,
        p: AMQP.BasicProperties?,
        body: ByteArray
    ) {
        super.handleDelivery(tag, e, p, body)
        val jsonString = String(body)
        val messageData = deserializer.fromJson(jsonString, Message::class.java)
        onMessageReceive(messageData)
        Timber.e("${e?.deliveryTag} ${messageData.data}")
    }
}
