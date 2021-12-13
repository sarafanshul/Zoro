package com.projectdelta.zoro.util.networking

object NetworkingConstants {

    private const val AVATAR_API = "https://avatars.dicebear.com/api/adventurer/"
    fun getAvatarURIByUserId( userId : String ) : String =
        "$AVATAR_API$userId.png"

    const val RABBIT_API = "10.0.2.2"
    const val RABBIT_PORT = 5672
    const val RABBIT_USER_NAME = "guest"
    const val RABBIT_USER_PASSWORD = "guest"

}