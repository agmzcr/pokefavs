package dev.agmzcr.pokefavs.data.model

import java.io.Serializable

data class PokemonTypes(
    val slot: Int,
    val type: PokemonResults
) : Serializable