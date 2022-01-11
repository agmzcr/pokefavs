package dev.agmzcr.pokefavs.ui.pokedex

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.agmzcr.pokefavs.data.model.PokemonResults
import dev.agmzcr.pokefavs.data.repository.PokemonRepository
import dev.agmzcr.pokefavs.util.Connection
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PokedexViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository
): ViewModel() {

    var currentResult: Flow<PagingData<PokemonResults>>? = null
    private var newResult: Flow<PagingData<PokemonResults>>? = null

    fun getPokemonList(query: String?): Flow<PagingData<PokemonResults>>? {

        newResult = pokemonRepository.getAllPokemonList(query).cachedIn(viewModelScope)
        currentResult = newResult

        return newResult
    }
}