/*
 * Copyright (c) 2022. Anshul Saraf
 */

package com.projectdelta.zoro.util.networking.amqp

import android.os.Looper
import com.google.gson.Gson
import com.projectdelta.zoro.data.model.Message
import com.projectdelta.zoro.util.Constants
import com.projectdelta.zoro.util.NotFound
import com.rabbitmq.client.AMQP
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import timber.log.Timber
import java.io.IOException
import java.util.concurrent.TimeoutException

/**
 * AMQP connection Client for RabbitMQ
 *
 * Usage
 * ```Kotlin
 * val client : RabbitMQClient = RabbitMQClient.getInstance( ..params.. )
 * client.registerChannel() // register is imp
 * ```
 * *To be used as a Singleton*
 *
 * Known Bugs
 * - When connection in closed due to Socket Timeout (no internetConnection)
 * , unregister fails to close connection and channel.
 * - Does not survives restart (due to change in theme etc)
 *
 * lifecycle needs to be on [onSTart - onStOp]
 */
class RabbitMQClient(
    host: String, port: Int,
    uName: String, password: String,
    private val deserializer: Gson
) : AMQPClient {

    companion object {

        private const val AUTO_ACKNOWLEDGMENT = true

        @Volatile
        private var INSTANCE: RabbitMQClient? = null

        fun getInstance(
            host: String, port: Int, uName: String, password: String, deserializer: Gson
        ): RabbitMQClient {
            val tempInstance = INSTANCE
            if (tempInstance != null)
                return tempInstance
            synchronized(this) {
                val instance = RabbitMQClient(host, port, uName, password, deserializer)
                INSTANCE = instance
                return instance
            }
        }
    }

    @Volatile
    private var connected: Boolean = false

    private lateinit var factory: ConnectionFactory
    private lateinit var connection: Connection
    private lateinit var channel: Channel

    init {
        setupConnection(host, port, uName, password)
    }

    override fun setupConnection(host: String, port: Int, uName: String, password: String) {
        factory = ConnectionFactory()
        factory.host = host
        factory.port = port
        factory.username = uName
        factory.password = password
        factory.isAutomaticRecoveryEnabled = false
        if( port == 5671 )
            factory.useSslProtocol()
    }

    override fun registerChannel() {
        if (!connected) {
            connected = true
            connection = factory.newConnection()
            channel = connection.createChannel()
            Timber.d("Channels registered")
        }
    }

    override fun registerAndConsume(queueName: String, doSomething: (m: Message?) -> Unit) {
        if (!connected) {

            connection = factory.newConnection()
            channel = connection.createChannel()
            connected = true
            try {
                channel.basicConsume(queueName, AUTO_ACKNOWLEDGMENT, AMQPConsumer(channel ,deserializer ,doSomething))
            } catch (exception: Exception) {
                when (exception) {
                    is IOException, is TimeoutException -> {
                        unregisterChannel(); Timber.d(exception)
                    }
                    else -> throw exception
                }
                connected = false
            }
            Timber.d("Channels registered")
        }
    }

    /**
     * Run this method in a IO Scope
     */
    @Throws(NotFound.ItsYourFaultIdiotException::class)
    @Deprecated("Use registerAndConsume instead")
    override fun consumeMessage(queue: String, doSomething: (m: Message?) -> Unit) {
        if (Thread.currentThread().equals(Looper.getMainLooper().thread))
            throw NotFound.ItsYourFaultIdiotException(Constants.WRONG_THREAD_EXCEPTION_IO)
        try {
            channel.basicConsume(queue, AUTO_ACKNOWLEDGMENT, AMQPConsumer(channel ,deserializer ,doSomething))
        } catch (exception: Exception) {
            when (exception) {
                is IOException, is TimeoutException -> {
                    unregisterChannel(); Timber.d(exception)
                }
                else -> throw exception
            }
        }
    }

    override fun unregisterChannel() {
        if (connected) {
            connected = false
//            try {
//                if (channel.isOpen) channel.close(AMQP.RESOURCE_ERROR, "channel force exit")
//            } catch (e: Exception) {
//                Timber.e(e)
//            }
            try {
                if (connection.isOpen) connection.close(AMQP.REPLY_SUCCESS, "connection force exit", 10)
            } catch (e: Exception) {
                Timber.e(e)
            }
            Timber.d("Channels unregistered")
        }
    }
}
