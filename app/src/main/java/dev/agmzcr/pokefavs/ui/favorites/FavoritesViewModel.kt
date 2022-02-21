package dev.agmzcr.pokefavs.ui.favorites

import android.util.Log
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.agmzcr.pokefavs.data.model.PokemonDetails
import dev.agmzcr.pokefavs.data.repository.DataStoreManager
import dev.agmzcr.pokefavs.data.repository.PokemonRepository
import dev.agmzcr.pokefavs.util.Filters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository,
    private val dataStore: DataStoreManager
): ViewModel() {

    private val _favoriteList = MutableLiveData<List<PokemonDetails>>()
    val favoriteList: LiveData<List<PokemonDetails>> = _favoriteList


    fun favoritesListOrderByIds() = viewModelScope.launch {
        _favoriteList.value = pokemonRepository.getAllSavedPokemonOrderByIds()
    }

    fun favoritesListOrderByNames() = viewModelScope.launch {
        _favoriteList.value = pokemonRepository.getAllSavedPokemonOrderByNames()
    }

    fun getFilters() = liveData(Dispatchers.IO) {
        dataStore.getFiltersFromPreferencesStore.collect {
            emit(it)
        }
    }

    fun setFilters(filters: Filters) = viewModelScope.launch {
            dataStore.saveFiltersToPreferencesStore(filters)
    }


    fun deletePokemon(id: Int) = viewModelScope.launch {
            pokemonRepository.deletePokemon(id)

    }

    fun undoDeletion(pokemon: PokemonDetails) = viewModelScope.launch {
            pokemonRepository.insertPokemon(pokemon)
    }
}