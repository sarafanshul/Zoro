package com.projectdelta.zoro.util.networking

object NetworkingConstants {

    // Avatar API
    private const val AVATAR_API = "https://avatars.dicebear.com/api/adventurer/"
    fun getAvatarURIByUserId(userId: String): String =
        "$AVATAR_API$userId.png"

    // DEV PROFILE
    const val DEV_PROFILE = "https://github.com/sarafanshul"

    // RabbitMQ API
    const val RABBIT_API = "10.0.2.2"
    const val RABBIT_PORT = 5672
    const val RABBIT_USER_NAME = "guest"
    const val RABBIT_USER_PASSWORD = "guest"

    // Luffy API
    const val BASE_URL = "http://10.0.2.2:8080"

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