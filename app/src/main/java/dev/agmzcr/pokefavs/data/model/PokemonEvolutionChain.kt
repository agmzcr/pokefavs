package dev.agmzcr.pokefavs.data.model

data class PokemonEvolutionChain(
    val evolution_details: List<PokemonEvolutionDetails>? = null,
    var evolves_to: List<PokemonEvolutionChain>? = null,
    val is_baby: Boolean? = null,
    val species: PokemonResults? = null
    )
