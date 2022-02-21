package dev.agmzcr.pokefavs.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.agmzcr.pokefavs.util.Filters
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DataStoreManager @Inject constructor(@ApplicationContext appContext: Context) {
    private val Context.dataStore by preferencesDataStore("filters")

    private val filtersDataStore = appContext.dataStore

    companion object {
        val SORTBY = intPreferencesKey("sortBy")
    }

    suspend fun saveFiltersToPreferencesStore(filters: Filters) {
        filtersDataStore.edit { preferences ->
            preferences[SORTBY] = filters.sortBy ?: 0
        }
    }

    val getFiltersFromPreferencesStore: Flow<Filters?> = filtersDataStore.data.map {
        val data = Filters(
            sortBy = it[SORTBY] ?: 0
        )
        data
    }
}