/*
 * Copyright (c) 2022. Anshul Saraf
 */

package com.projectdelta.zoro.data.preferences

import com.projectdelta.zoro.data.model.BaseDataModel

/**
 * Data Class as an abstract layer to act as typed datastore and for ease
 */
data class UserPreferences(
    val userId: String,
    val firstLogin: Boolean,
    val userName: String,
    val firstLoginTime: Long,
    val biometricEnabled: Boolean

) : BaseDataModel() {
    override fun copy() =
        UserPreferences(userId, firstLogin, userName, firstLoginTime, biometricEnabled)
}
