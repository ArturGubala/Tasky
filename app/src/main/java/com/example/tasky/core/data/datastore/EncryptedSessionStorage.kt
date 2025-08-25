package com.example.tasky.core.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.tasky.core.data.util.CryptoManager
import com.example.tasky.core.domain.datastore.AuthInfo
import com.example.tasky.core.domain.datastore.SessionStorage
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json

class EncryptedSessionStorage(
    context: Context,
    private val cryptoManager: CryptoManager
) : SessionStorage {

    companion object {
        private val Context.sessionDataStore: DataStore<Preferences> by preferencesDataStore(
            name = "encrypted_session"
        )
        private val AUTH_INFO_KEY = stringPreferencesKey("encrypted_auth_info")
    }

    private val dataStore = context.sessionDataStore
    private val json = Json.Default

    override suspend fun get(): AuthInfo? {
        return try {
            val encryptedData = dataStore.data.first()[AUTH_INFO_KEY]

            encryptedData?.let { encrypted ->
                cryptoManager.decryptObject<AuthInfo>(encrypted, json)
            }
        } catch (_: Exception) {
            null
        }
    }

    override suspend fun set(info: AuthInfo?) {
        try {
            if (info == null) {
                dataStore.edit { preferences ->
                    preferences.remove(AUTH_INFO_KEY)
                }
            } else {
                val encryptedData = cryptoManager.encryptObject(info, json)

                dataStore.edit { preferences ->
                    preferences[AUTH_INFO_KEY] = encryptedData
                }
            }
        } catch (_: Exception) {
            null
        }
    }
}
