package ru.boringowl.myroadmapapp.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "data_Storage")

@Singleton
class DataStorageImpl @Inject constructor(@ApplicationContext context: Context) : DataStorage {

    private val dataStore = context.dataStore

    private object PreferenceKeys{
        val SELECTED_THEME = stringPreferencesKey("selected_theme")
        val AUTH_TOKEN = stringPreferencesKey("auth_token")
    }

    override fun selectedTheme() = dataStore.data.map {
        it[PreferenceKeys.SELECTED_THEME] ?: "light"
    }


    override suspend fun setSelectedTheme(theme: String) {
        dataStore.edit {
            it[PreferenceKeys.SELECTED_THEME] = theme
        }
    }

    override fun authToken(): Flow<String> = dataStore.data.map {
        it[PreferenceKeys.AUTH_TOKEN] ?: ""
    }
    override suspend fun setAuthToken(token: String) {
        dataStore.edit {
            it[PreferenceKeys.AUTH_TOKEN] = token
        }
    }


}