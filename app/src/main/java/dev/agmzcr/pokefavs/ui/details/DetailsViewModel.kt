package dev.agmzcr.pokefavs.ui.details

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.agmzcr.pokefavs.data.model.PokemonDetails
import dev.agmzcr.pokefavs.data.model.PokemonResults
import dev.agmzcr.pokefavs.data.repository.PokemonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository,
    state: SavedStateHandle
): ViewModel() {

    val pokemonName = state.get<String>("pokemonName")

    private val _isSaved = MutableLiveData<Boolean>()
    val isSaved: LiveData<Boolean> = _isSaved

    private val _pokemonDetailsData = MutableLiveData<PokemonDetails?>(null)
    val pokemonDetailsData: LiveData<PokemonDetails?> = _pokemonDetailsData

    private val _pokemonDetailsDataToSend = MutableLiveData<PokemonDetails?>(null)
    val pokemonDetailsDataToSend: LiveData<PokemonDetails?> = _pokemonDetailsDataToSend

    private val _pokemonDetailsData2 = MutableLiveData<PokemonDetails>()
    val pokemonDetailsData2: LiveData<PokemonDetails> = _pokemonDetailsData2

    fun checkPokemon() {
        if (isPokemonSavedByName(pokemonName!!)) {
            getPokemonDetailsFromDbByName()
        } else {
            getPokemonDetailsFromApiByName()
        }
    }

    fun getDataViewPager(pokemon: PokemonDetails) {
        _pokemonDetailsData2.value = pokemon
    }

    private fun getPokemonDetailsFromDbById() = viewModelScope.launch {
            //_pokemonDetailsData.value = pokemonRepository.getSavedPokemonById()
            _isSaved.value = isPokemonSavedById(pokemonDetailsData.value!!.id!!)

    }

    private fun getPokemonDetailsFromDbByName() = viewModelScope.launch {
        _pokemonDetailsData.value = pokemonRepository.getSavedPokemonByName(pokemonName!!)
        _pokemonDetailsDataToSend.value = _pokemonDetailsData.value
        _isSaved.value = isPokemonSavedById(pokemonDetailsData.value!!.id!!)
    }

    private fun getPokemonDetailsFromApiByName() = viewModelScope.launch {
        _pokemonDetailsData.value = pokemonRepository.getPokemonData(pokemonName!!)
        _pokemonDetailsDataToSend.value = _pokemonDetailsData.value
        _isSaved.value = isPokemonSavedById(pokemonDetailsData.value!!.id!!)
    }

    fun isPokemonSavedById(id: Int): Boolean {
        _isSaved.value = pokemonRepository.isPokemonSavedById(id)
        return pokemonRepository.isPokemonSavedById(id)
    }

    fun isPokemonSavedByName(name: String): Boolean {
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

    fun getPokemonDetailsFromApiAgain(name: String) {
        viewModelScope.launch(Dispatchers.Main) {
            _pokemonDetailsData2.value = pokemonRepository.getPokemonData(name)
        }
    }

     fun getPokemonDetailsFromDbAgain(id: Int) = viewModelScope.launch {
            _pokemonDetailsData2.value = pokemonRepository.getSavedPokemonById(id)
         _isSaved.value = isPokemonSavedById(id)
    }

}