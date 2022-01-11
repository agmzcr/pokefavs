package dev.agmzcr.pokefavs.data.remote

import dev.agmzcr.pokefavs.data.model.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApi {
    @GET("pokemon")
    suspend fun getAllPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): PokemonResponse

    @GET("pokemon/{pokemonName}")
    suspend fun getPokemonDetails(
        @Path("pokemonName")
        pokemonName: String
    ): PokemonDetails

    @GET("pokemon-species/{pokemonName}")
    suspend fun getPokemonSpecies(
        @Path("pokemonName")
        pokemonName: String
    ): PokemonSpecies

    @GET("type/{typeName}")
    suspend fun getPokemonType(
        @Path("typeName")
        typeName: String
    ): PokemonType

    @GET("evolution-chain/{id}/")
    suspend fun getPokemonEvolution(
        @Path("id")
        id: Int
    ): PokemonEvolution
}