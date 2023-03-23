package com.example.orgs.infra.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.orgs.contracts.infra.preferences.UsuariosPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

data class UsuariosPreferencesImpl @Inject constructor(
    val context: Context,
): UsuariosPreferences {
    companion object {
        private const val USUARIO_PREFERENCES_NAME = "usuario_preferences"
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = USUARIO_PREFERENCES_NAME)
    }

    override suspend fun writeUsuarioName(usuarioName: String) {
        context.dataStore.edit { preferences ->
            preferences[USUARIO_NAME_KEY] = usuarioName
        }
    }

    override fun watchUsuarioName(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[USUARIO_NAME_KEY]
        }
    }

    override suspend fun removeUsuarioName() {
        context.dataStore.edit { preferences ->
            preferences.remove(USUARIO_NAME_KEY)
        }
    }
}

