package com.projectdelta.zoro.data.preferences

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferenceKeys {

    const val DATASTORE_NAME = "zoro_user_preferences"

    val USER_ID = stringPreferencesKey("user_id")
    val FIRST_LOGIN = booleanPreferencesKey("first_login")
}