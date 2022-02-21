package dev.agmzcr.pokefavs.ui.details

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.agmzcr.pokefavs.data.model.PokemonDetails
import dev.agmzcr.pokefavs.data.repository.PokemonRepository
import dev.agmzcr.pokefavs.util.UIState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository,
    stateHandle: SavedStateHandle
): ViewModel() {

    val pokemonName = stateHandle.get<String>("pokemonName")

    private val _isSaved = MutableLiveData<Boolean>()
    val isSaved: LiveData<Boolean> = _isSaved

    private val _state = MutableLiveData<UIState>()
    val state: LiveData<UIState> = _state

    private val _pokemonDetailsData = MutableLiveData<PokemonDetails>()
    val pokemonDetailsData: LiveData<PokemonDetails?> = _pokemonDetailsData

    //private val _pokemonDetailsDataToSend = MutableLiveData<PokemonDetails?>(null)
    //val pokemonDetailsDataToSend: LiveData<PokemonDetails?> = _pokemonDetailsDataToSend


    fun checkPokemon() {
        if (isPokemonSavedByName(pokemonName!!)) {
            getPokemonDetailsFromDbByName()
        } else {
            getPokemonDetailsFromApiByName()
        }
    }

    fun getDataViewPager(pokemon: PokemonDetails) {
        _pokemonDetailsData.value = pokemon
    }


    private fun getPokemonDetailsFromDbByName() {
        viewModelScope.launch {
            _state.postValue(UIState.Loading)
            try {
                pokemonRepository.getSavedPokemonByName(pokemonName!!).let {
                    _state.postValue(UIState.Success(it))
                    _isSaved.value = isPokemonSavedById(it.id!!)
                }
            } catch (e: Exception) {
                _state.postValue(UIState.Error("Error"))
            }
        }
    }


    private fun getPokemonDetailsFromApiByName() {
        viewModelScope.launch {
            _state.postValue(UIState.Loading)
            try {
                pokemonRepository.getPokemonData(pokemonName!!).let {
                    _state.postValue(UIState.Success(it))
                    _isSaved.value = isPokemonSavedById(it.id!!)
                }
            } catch (e: Exception) {
                _state.postValue(UIState.Error(e.message.toString()))
            }
        }
    }

    private fun isPokemonSavedById(id: Int): Boolean {
        _isSaved.value = pokemonRepository.isPokemonSavedById(id)
        return pokemonRepository.isPokemonSavedById(id)
    }

    private fun isPokemonSavedByName(name: String): Boolean {
        _isSaved.value = pokemonRepository.isPokemonSavedByName(name)
        return pokemonRepository.isPokemonSavedByName(name)
    }

    fun insertPokemon(pokemon: PokemonDetails) =
        viewModelScope.launch {
            pokemonRepository.insertPokemon(pokemon)
        }

    fun deletePokemon(id: Int) =
        viewModelScope.launch {
            pokemonRepository.deletePokemon(id)
        }
}