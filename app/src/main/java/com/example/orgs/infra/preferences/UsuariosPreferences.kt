package com.example.orgs.infra.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class UsuariosPreferences(val context: Context) {
    companion object {
        private const val USUARIO_PREFERENCES_NAME = "usuario_preferences"
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = USUARIO_PREFERENCES_NAME)
    }

    suspend fun writeUsuarioName(usuarioName: String) {
        context.dataStore.edit { preferences ->
            preferences[USUARIO_NAME_KEY] = usuarioName
        }
    }

    fun watchUsuarioName(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[USUARIO_NAME_KEY]
        }
    }

    suspend fun removeUsuarioName() {
        context.dataStore.edit { preferences ->
            preferences.remove(USUARIO_NAME_KEY)
        }
    }
}

