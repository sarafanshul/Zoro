package com.projectdelta.zoro.util.networking.amqp

import android.os.Looper
import com.google.gson.Gson
import com.projectdelta.zoro.data.model.Message
import com.projectdelta.zoro.util.Constants
import com.projectdelta.zoro.util.NotFound
import com.rabbitmq.client.*
import timber.log.Timber
import java.io.IOException
import java.util.concurrent.TimeoutException

/**
 * AMQP connection Client for RabbitMQ
 *
 * To be used as a Singleton
 *
 * Usage
 * ```Kotlin
 * val client : RabbitMQClient = RabbitMQClient.getInstance( ..params.. )
 * client.registerChannel() // register is imp
 * ```
 *
 * Known Bugs
 * - When connection in closed due to Socket Timeout (no internetConnection)
 * , unregister fails to close connection and channel.
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

    private fun setupConnection(host: String, port: Int, uName: String, password: String) {
        factory = ConnectionFactory()
        factory.host = host
        factory.port = port
        factory.username = uName
        factory.password = password
    }

    override fun registerChannel() {
        if (!connected) {
            connected = true
            connection = factory.newConnection()
            channel = connection.createChannel()
            Timber.d("Channels registered")
        }
    }

    /**
     * Run this method in a IO Scope
     */
    @Throws(NotFound.ItsYourFaultIdiotException::class)
    override fun consumeMessage(queue: String, doSomething: (m: Message?) -> Unit) {
        if (Thread.currentThread().equals(Looper.getMainLooper().thread))
            throw NotFound.ItsYourFaultIdiotException(Constants.WRONG_THREAD_EXCEPTION_IO)
        try {
            channel.basicConsume(queue, AUTO_ACKNOWLEDGMENT,
                object : DefaultConsumer(channel) {
                    override fun handleDelivery(
                        tag: String,
                        e: Envelope?,
                        p: AMQP.BasicProperties?,
                        body: ByteArray
                    ) {
                        super.handleDelivery(tag, e, p, body)
                        val jsonString = String(body)
                        val messageData = deserializer.fromJson(jsonString, Message::class.java)
                        doSomething(messageData)
                        Timber.e("${e?.deliveryTag} ${messageData.data}")
                    }
                }
            )
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
            try {
                if (channel.isOpen) channel.close(AMQP.RESOURCE_ERROR, "no internet")
            } catch (e: Exception) {
                Timber.d(e)
            }
            try {
                if (connection.isOpen) connection.close(AMQP.RESOURCE_ERROR, "no internet", 10)
            } catch (e: Exception) {
                Timber.d(e)
            }
            Timber.d("Channels unregistered")
        }
    }
}