package dev.agmzcr.pokefavs.data.model

data class PokemonResponse(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<PokemonResults>
)