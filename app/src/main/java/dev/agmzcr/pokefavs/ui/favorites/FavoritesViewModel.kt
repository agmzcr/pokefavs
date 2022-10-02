package dev.agmzcr.pokefavs.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.agmzcr.pokefavs.data.model.PokemonDetails
import dev.agmzcr.pokefavs.data.repository.DataStoreManager
import dev.agmzcr.pokefavs.data.repository.PokemonRepository
import dev.agmzcr.pokefavs.util.Filters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository,
    private val dataStore: DataStoreManager
): ViewModel() {

    fun favorites(orderBy: String): LiveData<List<PokemonDetails>> {
        return pokemonRepository.getAllFavoritesPokemons(orderBy)
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