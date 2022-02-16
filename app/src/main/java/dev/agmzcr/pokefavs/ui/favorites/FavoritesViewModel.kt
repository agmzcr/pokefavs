package dev.agmzcr.pokefavs.ui.favorites

import android.util.Log
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.agmzcr.pokefavs.data.model.PokemonDetails
import dev.agmzcr.pokefavs.data.repository.DataStoreManager
import dev.agmzcr.pokefavs.data.repository.PokemonRepository
import dev.agmzcr.pokefavs.util.Filters
import dev.agmzcr.pokefavs.util.UIState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository,
    private val dataStore: DataStoreManager
): ViewModel() {

    private val _favoritesList = MutableLiveData<UIState>()
    val favoritesList: LiveData<UIState> = _favoritesList

    val favoritesListOrderByIds = pokemonRepository.getAllSavedPokemonOrderByIds().asLiveData()
    val favoritesListOrderByNames = pokemonRepository.getAllSavedPokemonOrderByNames().asLiveData()

    var filtersDefault: Filters = Filters.default

    fun favoritesListOrderByIds() {
        viewModelScope.launch {
            _favoritesList.postValue(UIState.Loading)
            try {
                pokemonRepository.getAllSavedPokemonOrderByIds().collect {
                    _favoritesList.postValue(UIState.Success(it))
                }
            } catch (e:Exception) {
                _favoritesList.postValue(UIState.Error(e.message.toString()))
            }
        }
    }

    fun favoritesListOrderByNames() {
        viewModelScope.launch {
            _favoritesList.postValue(UIState.Loading)
            try {
                pokemonRepository.getAllSavedPokemonOrderByIds().collect {
                    _favoritesList.postValue(UIState.Success(it))
                }
            } catch (e:Exception) {
                _favoritesList.postValue(UIState.Error(e.message.toString()))
            }
        }
    }

    fun setFilters(filters: Filters) {
        viewModelScope.launch {
            dataStore.saveFiltersToPreferencesStore(filters)
        }
    }

    fun getFilters(): Filters? {
        var data = Filters()
        viewModelScope.launch {
            dataStore.getFiltersFromPreferencesStore.collect { data = it!! }
        }
        return data
    }

    fun deletePokemon(id: Int) {
        viewModelScope.launch {
            pokemonRepository.deletePokemon(id)
        }
    }

    fun undoDeletion(pokemon: PokemonDetails) {
        viewModelScope.launch {
            pokemonRepository.insertPokemon(pokemon)
        }
    }
}