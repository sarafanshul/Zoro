package com.projectdelta.zoro.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.projectdelta.zoro.data.model.MessageData
import com.projectdelta.zoro.data.preferences.UserPreferences
import com.projectdelta.zoro.databinding.ActivityMainBinding
import com.projectdelta.zoro.ui.base.BaseViewBindingActivity
import com.projectdelta.zoro.util.Constants.generateUniqueUserId
import com.projectdelta.zoro.util.networking.NetworkingConstants.RABBIT_API
import com.projectdelta.zoro.util.networking.NetworkingConstants.RABBIT_PORT
import com.projectdelta.zoro.util.networking.NetworkingConstants.RABBIT_USER_NAME
import com.projectdelta.zoro.util.networking.NetworkingConstants.RABBIT_USER_PASSWORD
import com.projectdelta.zoro.util.networking.NetworkingConstants.getAvatarURIByUserId
import com.rabbitmq.client.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import java.util.concurrent.TimeoutException

class MainActivity : BaseViewBindingActivity<ActivityMainBinding>() {

    private val _data : MutableLiveData<UserPreferences> = MutableLiveData()
    private val data : LiveData<UserPreferences> = _data

    private lateinit var factory: ConnectionFactory

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setupConnectionFactory()

        lifecycleScope.launch {
            preferenceManager.preferenceFlow.collectLatest {
                _data.postValue(it)
            }
        }

        lifecycleScope.launch (Dispatchers.IO){
            basicConsumer { m, _ ->
                runOnUiThread {
                    binding.msg.text = m?.data
                }
            }
        }

        data.observe(this){
            Glide.with(this)
                .load(getAvatarURIByUserId(it.userId))
//                .apply(RequestOptions.circleCropTransform())
                .into(binding.test)
        }

        binding.tap.setOnClickListener{
            lifecycleScope.launch{
                preferenceManager.updateUserId( generateUniqueUserId() )
            }
        }

    }

    private fun setupConnectionFactory(){
        factory = ConnectionFactory()
        factory.host = RABBIT_API
        factory.port = RABBIT_PORT
        factory.username = RABBIT_USER_NAME
        factory.password = RABBIT_USER_PASSWORD
        Timber.d("Setup Connection Factory ")
    }

    private fun basicConsumer( doSomething : (m : MessageData? ,d : Long?) -> Unit ){
        try{
            val connection : Connection = factory.newConnection()

            val channel : Channel = connection.createChannel()
            Timber.d("Channel is Open: ${channel.isOpen}")
            channel.basicConsume("1231" ,true , object : DefaultConsumer(channel) {
                override fun handleDelivery(
                    consumerTag: String?,
                    envelope: Envelope?,
                    properties: AMQP.BasicProperties?,
                    body: ByteArray?
                ) {
                    super.handleDelivery(consumerTag, envelope, properties, body)

                    val deliveryTag = envelope?.deliveryTag
                    val str = String(body!!)
                    val message : MessageData = Gson().fromJson(str ,MessageData::class.java)
                    Timber.d("$message ,$deliveryTag" )
                    doSomething(message ,deliveryTag)
                }
            })
        }
        catch (exception : IOException){
            Timber.e(exception)
        }
        catch (exception : TimeoutException){
            Timber.e(exception)
        }
    }
}