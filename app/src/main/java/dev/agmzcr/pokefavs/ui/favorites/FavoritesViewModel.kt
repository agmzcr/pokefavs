package dev.agmzcr.pokefavs.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.agmzcr.pokefavs.data.model.PokemonDetails
import dev.agmzcr.pokefavs.data.repository.PokemonRepository
import dev.agmzcr.pokefavs.util.Filters
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository
): ViewModel() {

    val favoritesList = pokemonRepository.getAllSavedPokemon()
    val favoritesListOrderByNames = pokemonRepository.getAllSavedPokemonOrderByNames()
    var filters: Filters = Filters.default

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