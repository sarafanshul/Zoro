/*
 * Copyright (c) 2022. Anshul Saraf
 */

package com.projectdelta.zoro.util.networking

import com.projectdelta.zoro.BuildConfig

object NetworkingConstants {

    // Avatar API
    private const val AVATAR_API = BuildConfig.AVATAR_API
    fun getAvatarURIByUserId(userId: String): String =
        "$AVATAR_API$userId.png"

    // DEV PROFILE
    const val DEV_PROFILE = "https://github.com/sarafanshul"

    // RabbitMQ API
    const val RABBIT_API = BuildConfig.RABBITMQ_HOST
    val RABBIT_PORT = BuildConfig.RABBITMQ_PORT.toInt()
    const val RABBIT_USER_NAME = BuildConfig.RABBIT_USER_NAME
    const val RABBIT_USER_PASSWORD = BuildConfig.RABBIT_USER_PASSWORD

    // Luffy API
    const val BASE_URL = BuildConfig.API_BASE_URL

    const val USER_URL = "user/"
    const val MESSAGE_URL = "message/"

    const val SUB_URL_GET = "get"
    const val SUB_URL_ADD_USER = "nakama"
    const val SUB_URL_SEND = "send"
    const val SUB_URL_COUNT = "count"
    const val SUB_URL_FRIENDS = "tomodachi"
    const val SUB_URL_DISCONNECT = "nakama_bye"

    const val QUERY_QUEUE = "queue"
    const val QUERY_ID = "id"
    const val QUERY_USER_ID = "userId"
}
