package com.dicoding.storyapp.localdata

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    private val ID_KEY = stringPreferencesKey("user_id_key")
    private val NAME_KEY = stringPreferencesKey("user_name_key")
    private val TOKEN_KEY = stringPreferencesKey("user_token_key")

    fun getUserId(): Flow<String> {
        return dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[ID_KEY] ?: ""
        }
    }

    fun getUserName(): Flow<String> {
        return dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[NAME_KEY] ?: ""
        }
    }

    fun getUserToken(): Flow<String> {
        return dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[TOKEN_KEY] ?: ""
        }
    }

    fun isUserDataExists(): Flow<Boolean> {
        return dataStore.data.catch{ exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preference ->
            preference.contains(ID_KEY) and
            preference.contains(NAME_KEY) and
            preference.contains(TOKEN_KEY)
        }
    }

    suspend fun saveUserPreferences(userId: String, userName: String, userToken: String) {
        dataStore.edit { preferences ->
            preferences[ID_KEY] = userId
            preferences[NAME_KEY] = userName
            preferences[TOKEN_KEY] = userToken
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}