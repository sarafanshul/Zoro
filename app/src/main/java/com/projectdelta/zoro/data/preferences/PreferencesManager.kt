package com.projectdelta.zoro.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException

/**
 * Top level declaration as per [this](https://developer.android.com/jetpack/androidx/releases/datastore#1.0.0-alpha07)
 */
private val Context.dataStore by preferencesDataStore(PreferenceKeys.DATASTORE_NAME)

@Suppress("unused")
class PreferencesManager(
    private val context: Context
) {

    val preferenceFlow : Flow<UserPreferences>
        get() = context.dataStore.data
            .catch { exception ->
                if( exception is IOException ){
                    Timber.e(exception ,"Error reading exception")
                    emit( emptyPreferences() )
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val userId : String = preferences[PreferenceKeys.USER_ID] ?: ""
                val firstLogin : Boolean = preferences[PreferenceKeys.FIRST_LOGIN] ?: true
                UserPreferences(userId, firstLogin)
            }

    suspend fun updateUserId( id : String ){
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.USER_ID] = id
        }
    }

    suspend fun updateFirstLogin(status : Boolean ){
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.FIRST_LOGIN] = status
        }
    }

}