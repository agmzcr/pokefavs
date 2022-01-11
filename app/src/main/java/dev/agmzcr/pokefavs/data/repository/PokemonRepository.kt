package dev.agmzcr.pokefavs.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dev.agmzcr.pokefavs.data.local.FavoritesDatabase
import dev.agmzcr.pokefavs.data.model.*
import dev.agmzcr.pokefavs.data.remote.PokeApi
import dev.agmzcr.pokefavs.ui.pokedex.PokedexPagingSource
import dev.agmzcr.pokefavs.util.extractPokemonIdFromUrlEvolution
import dev.agmzcr.pokefavs.util.extractPokemonIdFromUrlSpecies
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PokemonRepository @Inject constructor(
    private val api: PokeApi,
    private val db: FavoritesDatabase
) {

    // Remote

    fun getAllPokemonList(query: String?): Flow<PagingData<PokemonResults>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PokedexPagingSource(api, query) }
        ).flow
    }

    suspend fun getPokemonData(pokemonName: String): PokemonDetails {
            val pokemon = api.getPokemonDetails(pokemonName)
            val species = api.getPokemonSpecies(pokemonName)

            pokemon.base_happiness = species.base_happiness
            pokemon.capture_rate = species.capture_rate
            pokemon.description = species.flavor_text_entries[20].flavor_text.replace("\n", " ")
            pokemon.genera = species.genera[7].genus
            pokemon.habitat = species.habitat.name!!

            val dataFromEvolutions = api.getPokemonEvolution(species.evolution_chain?.url?.extractPokemonIdFromUrlEvolution()!!)
            val evolutionsList = extractEvolutions(dataFromEvolutions.chain)
            val evolutionsMapped: List<PokemonEvolutions> = evolutionsList.map { PokemonEvolutions(it.first, it.second) }
            pokemon.evolutions = evolutionsMapped

        return pokemon
    }


    // Need more work on this
    private fun extractEvolutions(
        chain: PokemonEvolutionChain
        ): ArrayList<Pair<Int, String>> {

        val list = ArrayList<Pair<Int, String>>(emptyList())
        val mainChain: PokemonEvolutionChain = chain


        if (mainChain.evolves_to != null) {
            list.add(Pair(mainChain.species!!.url!!.extractPokemonIdFromUrlSpecies(), mainChain.species.name!!))

                for (evolutionChain in mainChain.evolves_to!!) {
                    if (evolutionChain.evolves_to != null) {
                        list.add(Pair(evolutionChain.species!!.url!!.extractPokemonIdFromUrlSpecies(), evolutionChain.species.name!!))

                        for (evolutionChain2 in evolutionChain.evolves_to!!) {
                            if (evolutionChain2.evolves_to != null) {
                            list.add(Pair(evolutionChain2.species!!.url!!.extractPokemonIdFromUrlSpecies(), evolutionChain2.species.name!!))
                        } else {
                            evolutionChain.evolves_to!!.forEach { _ ->
                                //TODO
                            }
                            break
                        }
                    }
                }
            }
        }
        return list
    }

    // Room

    suspend fun insertPokemon(pokemon: PokemonDetails) =
        db.favoritesDao().insert(pokemon)

    fun isPokemonSavedById(id : Int) : Boolean {
        return db.favoritesDao().isPokemonSavedById(id)
    }

    fun isPokemonSavedByName(name: String) : Boolean {
        return db.favoritesDao().isPokemonSavedByName(name)
    }

    fun getAllSavedPokemon(): LiveData<List<PokemonDetails>> =
        db.favoritesDao().getAllSavedPokemon()

    suspend fun getSavedPokemonById(id: Int) =
        db.favoritesDao().getSavedPokemonById(id)

    suspend fun getSavedPokemonByName(name: String) =
        db.favoritesDao().getSavedPokemonByName(name)

    suspend fun deletePokemon(id: Int) =
        db.favoritesDao().deletePokemon(id)
}