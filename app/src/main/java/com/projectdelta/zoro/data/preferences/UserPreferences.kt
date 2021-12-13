package com.projectdelta.zoro.data.preferences

import java.util.*

/**
 * Data Class as an abstract layer to act as typed datastore and for ease
 */
data class UserPreferences(
    val userId : String ,
    val firstLogin : Boolean
)