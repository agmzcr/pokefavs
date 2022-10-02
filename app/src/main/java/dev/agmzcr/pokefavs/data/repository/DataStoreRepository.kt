package dev.agmzcr.pokefavs.data.repository

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.agmzcr.pokefavs.util.Filters
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("user_preferences")

@Singleton
class DataStoreManager @Inject constructor(@ApplicationContext appContext: Context) {
    private val dataStore = appContext.dataStore

    companion object {
        val sortBy = stringPreferencesKey("sort_by")
        val uiMode = intPreferencesKey("ui_mode")
    }

    suspend fun saveUiMode(uiModeInt: Int) {
        dataStore.edit { preferences ->
            preferences[uiMode] = uiModeInt
        }
    }

    val getUiMode : Flow<Int> = dataStore.data.map {
        val data = it[uiMode] ?: AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        data
    }

    suspend fun saveFiltersToPreferencesStore(filters: Filters) {
        dataStore.edit { preferences ->
            preferences[sortBy] = filters.sortBy ?: "id"
        }
    }

    val getFiltersFromPreferencesStore: Flow<Filters?> = dataStore.data.map {
        val data = Filters(
            sortBy = it[sortBy] ?: "id"
        )
        data
    }
}