package dev.agmzcr.pokefavs.ui.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.agmzcr.pokefavs.data.repository.DataStoreManager
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStore: DataStoreManager
): ViewModel() {
    fun setUiMode(ui: Int) = viewModelScope.launch {
        dataStore.saveUiMode(ui)
        Log.i("setUiModeVW", ui.toString())
    }
}