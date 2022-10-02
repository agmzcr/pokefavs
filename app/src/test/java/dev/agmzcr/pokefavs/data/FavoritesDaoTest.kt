package dev.agmzcr.pokefavs.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.agmzcr.pokefavs.data.local.FavoritesDao
import dev.agmzcr.pokefavs.data.model.*

class FavoritesDaoTest : FavoritesDao {

    private val ability = PokemonAbility("damage")
    private val type = PokemonResults("fire")
    private val abilities: List<PokemonAbilities> =
        listOf(PokemonAbilities(0, ability), PokemonAbilities(1, ability))
    private val stats: List<PokemonStats> =
        listOf(PokemonStats(1), PokemonStats(2))
    private val types: List<PokemonTypes> =
        listOf(PokemonTypes(0, type))
    private val evolutions: List<PokemonEvolutions> =
        listOf(PokemonEvolutions(0, "bulbasur"), PokemonEvolutions(1, "charmander"))
    private val weaknesses: List<String> =
        listOf("fire", "water")

    private val pokemon1 = PokemonDetails(0, "bulbasur", abilities,
        stats, types, 2, 3, 4, 5, 6,
        "description", "genera", "habitat", evolutions, weaknesses)

    private val pokemon2 = PokemonDetails(1, "charmander", abilities,
        stats, types, 2, 3, 4, 5, 6,
        "description", "genera", "habitat", evolutions, weaknesses)

    private val pokemons = MutableLiveData(listOf(pokemon1, pokemon2))

    override suspend fun insert(pokemon: PokemonDetails) {
        pokemons.value = pokemons.value?.toMutableList()?.apply { add(pokemon) }
    }

    override suspend fun delete(pokemonId: Int) {
        pokemons.value = pokemons.value?.toMutableList()?.apply { delete(pokemonId) }
    }

    override fun getAllSavedPokemonByIds(): LiveData<List<PokemonDetails>> = pokemons

    override fun getAllSavedPokemonOrderByNames(): LiveData<List<PokemonDetails>> = pokemons

    override fun getSavedPokemonByName(name: String): PokemonDetails {
        var poke = PokemonDetails()
            pokemons.value!!.forEach {
            if (it.name == name) {
                poke = it
            }
        }
        return poke
    }
}