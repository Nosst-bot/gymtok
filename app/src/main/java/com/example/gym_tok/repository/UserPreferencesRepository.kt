package com.example.gym_tok.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.gym_tok.model.UserDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferencesRepository private constructor(context: Context) {

    private val dataStore = context.dataStore

    companion object {
        // --- 1. AÑADIMOS UNA LLAVE PARA EL ID DEL USUARIO ---
        val USER_ID_KEY = intPreferencesKey("user_id")
        val USER_NAME_KEY = stringPreferencesKey("user_name")
        val USER_EMAIL_KEY = stringPreferencesKey("user_email")


        @Volatile
        private var INSTANCE: UserPreferencesRepository? = null

        fun getInstance(context: Context): UserPreferencesRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserPreferencesRepository(context).also { INSTANCE = it }
            }
        }
    }

    // --- 2. ACTUALIZAMOS LA FUNCIÓN PARA QUE TAMBIÉN GUARDE EL ID ---
    suspend fun saveUserSession(user: UserDTO) {
        dataStore.edit { preferences ->
            // Asumimos que tu UserDTO ahora tendrá un campo "id" de tipo Int
            preferences[USER_ID_KEY] = user.id
            preferences[USER_NAME_KEY] = user.userName
            preferences[USER_EMAIL_KEY] = user.email
        }
    }

    // --- 3. CREAMOS LA "TUBERÍA" (FLOW) PARA EXPONER EL ID ---
    val userId: Flow<Int?> = dataStore.data.map { preferences ->
        preferences[USER_ID_KEY]
    }

    val userName: Flow<String?> = dataStore.data.map { preferences ->
        preferences[USER_NAME_KEY]
    }

    suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }


}
