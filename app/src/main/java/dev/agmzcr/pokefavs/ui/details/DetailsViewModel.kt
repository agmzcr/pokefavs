package dev.agmzcr.pokefavs.ui.details

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.agmzcr.pokefavs.data.model.PokemonDetails
import dev.agmzcr.pokefavs.data.repository.PokemonRepository
import dev.agmzcr.pokefavs.util.UIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
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

    private val _pokemonDetailsDataToSend = MutableLiveData<PokemonDetails?>(null)
    val pokemonDetailsDataToSend: LiveData<PokemonDetails?> = _pokemonDetailsDataToSend

    /*private val _pokemonDetailsData2 = MutableLiveData<PokemonDetails>()
    val pokemonDetailsData2: LiveData<PokemonDetails> = _pokemonDetailsData2*/

    fun checkPokemon() {
        if (isPokemonSavedByName(pokemonName!!)) {
            //getPokemonDetailsFromDbByName()
            getPokemonDetailsFromDbByName2()
        } else {
            //getPokemonDetailsFromApiByName()
            getPokemonDetailsFromApiByName2()
        }
    }

    fun getDataViewPager(pokemon: PokemonDetails) {
        _pokemonDetailsData.value = pokemon
    }

    private fun getPokemonDetailsFromDbById() = viewModelScope.launch {
            _isSaved.value = isPokemonSavedById(pokemonDetailsData.value!!.id!!)
    }

    /*private fun getPokemonDetailsFromDbByName() = viewModelScope.launch {
         pokemonRepository.getSavedPokemonByName(pokemonName!!).collect {
             _pokemonDetailsData.value = it
             _pokemonDetailsDataToSend.value = it
             _isSaved.value = isPokemonSavedById(it.id!!)
         }
    }*/

    private fun getPokemonDetailsFromDbByName2() {
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

    private fun getPokemonDetailsFromApiByName() = viewModelScope.launch {
        _pokemonDetailsData.value = pokemonRepository.getPokemonData(pokemonName!!)
        _pokemonDetailsDataToSend.value = _pokemonDetailsData.value
        _isSaved.value = isPokemonSavedById(pokemonDetailsData.value!!.id!!)
    }

    private fun getPokemonDetailsFromApiByName2() {
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

    /*fun getPokemonDetailsFromApiAgain(name: String) {
        viewModelScope.launch(Dispatchers.Main) {
            _pokemonDetailsData2.value = pokemonRepository.getPokemonData(name)
        }
    }*/
}