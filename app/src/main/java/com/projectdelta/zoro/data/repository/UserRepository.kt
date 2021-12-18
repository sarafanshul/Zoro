package com.projectdelta.zoro.data.repository

import com.projectdelta.zoro.data.model.User

interface UserRepository {

    suspend fun getUserById( userId : String ) : User?

    suspend fun updateUser( user : User ) : User?

    suspend fun getFriends( userId : String ) : List<User>
}