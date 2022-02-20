package dev.agmzcr.pokefavs.ui.favorites

import android.util.Log
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.agmzcr.pokefavs.data.model.PokemonDetails
import dev.agmzcr.pokefavs.data.repository.DataStoreManager
import dev.agmzcr.pokefavs.data.repository.PokemonRepository
import dev.agmzcr.pokefavs.util.Filters
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository,
    private val dataStore: DataStoreManager
): ViewModel() {

    var filters: Filters? = null

    lateinit var favoriteList: LiveData<List<PokemonDetails>>

    init {
        getFilters()
    }

    fun favoritesListOrderByIds() = viewModelScope.launch {
        favoriteList = pokemonRepository.getAllSavedPokemonOrderByIds()
    }

    fun favoritesListOrderByNames() = viewModelScope.launch {
        favoriteList = pokemonRepository.getAllSavedPokemonOrderByNames()
    }

    fun setFilters(filters: Filters) = viewModelScope.launch {
            dataStore.saveFiltersToPreferencesStore(filters)
    }

    private fun getFilters() = viewModelScope.launch {
            dataStore.getFiltersFromPreferencesStore.collect {
                    filters = it
        }
    }

    fun deletePokemon(id: Int) = viewModelScope.launch {
            pokemonRepository.deletePokemon(id)

    }

    fun undoDeletion(pokemon: PokemonDetails) = viewModelScope.launch {
            pokemonRepository.insertPokemon(pokemon)
    }
}