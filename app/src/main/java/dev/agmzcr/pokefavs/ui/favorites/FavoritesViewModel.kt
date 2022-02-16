package dev.agmzcr.pokefavs.ui.favorites

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

    init {
        getFilters()
    }

    private val _filters = MutableLiveData<Filters>()
    val filters: LiveData<Filters> = _filters

    private val _favoriteList = MutableLiveData<List<PokemonDetails>>()
    val favoriteList: LiveData<List<PokemonDetails>> = _favoriteList

    val favoritesListOrderByIds = pokemonRepository.getAllSavedPokemonOrderByIds()
    val favoritesListOrderByNames = pokemonRepository.getAllSavedPokemonOrderByNames()

    var filtersDefault: Filters = Filters.default


    fun favoritesListOrderByIds() {
        viewModelScope.launch {
            _favoriteList.value = pokemonRepository.getAllSavedPokemonOrderByIds()
        }
    }

    fun favoritesListOrderByNames() {
        viewModelScope.launch {
            _favoriteList.value = pokemonRepository.getAllSavedPokemonOrderByNames()
        }
    }

    fun setFilters(filters: Filters) {
        viewModelScope.launch {
            dataStore.saveFiltersToPreferencesStore(filters)
        }
    }

    private fun getFilters() {
        viewModelScope.launch {
            dataStore.getFiltersFromPreferencesStore.collect {
                if (it == null) {
                    _filters.postValue(Filters.default)
                } else {
                    _filters.postValue(it)
                }
            }
        }
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