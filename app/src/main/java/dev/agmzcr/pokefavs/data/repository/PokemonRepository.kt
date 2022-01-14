package dev.agmzcr.pokefavs.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
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

    var damagesFirstType = PokemonType()
    var damagesSecondType = PokemonType()

    // Remote

    fun getAllPokemonList(query: String?): Flow<PagingData<PokemonResults>> {
        return Pager(
            config = PagingConfig(
                pageSize = 30,
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

            val flavorTextList = species.flavor_text_entries
            if (flavorTextList != null) {
                for (flavorText in flavorTextList) {
                    if (flavorText.language?.name.equals("en") &&
                        flavorText.version?.name.equals("black-2")) {
                    pokemon.description = flavorText.flavor_text?.replace("\n", " ")
                    break
                    }
                }
            }
            pokemon.genera = species.genera?.get(7)?.genus
            pokemon.habitat = species.habitat?.name

            val dataFromEvolutions = api.getPokemonEvolution(species.evolution_chain?.url?.extractPokemonIdFromUrlEvolution()!!)
            val evolutionsList = extractEvolutions(dataFromEvolutions.chain)
            val evolutionsMapped: List<PokemonEvolutions> = evolutionsList.map { PokemonEvolutions(it.first, it.second) }
            pokemon.evolutions = evolutionsMapped

            if (pokemon.types?.size == 1) {
                damagesFirstType = pokemon.types?.get(0)?.type?.name?.let { api.getPokemonType(it) }!!
            } else {
                damagesFirstType = pokemon.types?.get(0)?.type?.name?.let { api.getPokemonType(it) }!!
                damagesSecondType = pokemon.types?.get(1)?.type?.name?.let { api.getPokemonType(it) }!!
            }
            pokemon.weaknesses = extractWeakness(damagesFirstType?.damage_relations, damagesSecondType?.damage_relations)

        return pokemon
    }

    private fun extractWeakness(
        firstType: PokemonDamage?,
        secondType: PokemonDamage?
    ): ArrayList<String> {
        val list = ArrayList<String>(emptyList())
        val listFirstTypeDouble = ArrayList<String>(emptyList())
        val listFirstTypeHalf = ArrayList<String>(emptyList())
        val listSecondTypeDouble = ArrayList<String>(emptyList())
        val listSecondTypeHalf = ArrayList<String>(emptyList())

        if (firstType != null) {
            for (type in firstType.double_damage_from) {
                type.name?.let { listFirstTypeDouble.add(it) }
            }
            Log.i("FIRSTTYPELIST", listFirstTypeDouble.toString())

            for (type in firstType.half_damage_from) {
                type.name?.let { listFirstTypeHalf.add(it) }
            }
            Log.i("FIRSTTYPELISTHALF", listFirstTypeHalf.toString())
        }

        if (secondType != null) {
            for (type in secondType.double_damage_from) {
                type.name?.let { listSecondTypeDouble.add(it) }
            }
            Log.i("SECONDTYPELIST", listSecondTypeDouble.toString())

            for (type in secondType.half_damage_from) {
                type.name?.let { listSecondTypeHalf.add(it) }
            }
            Log.i("SECONDTYPELISTHALF", listSecondTypeHalf.toString())
        }

        val listFilter = listFirstTypeDouble.toSet().minus(listSecondTypeHalf.toSet())
        val listFilter2 = listSecondTypeDouble.toSet().minus(listFirstTypeHalf.toSet())

        list.addAll(listFilter + listFilter2)
        Log.i("FINISHLIST", list.toString())
        //Log.i("FINISHLIST", listFilter.toString())
        //Log.i("FINISHLIST2", listFilter2.toString())

        return list
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