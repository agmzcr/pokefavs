package dev.agmzcr.pokefavs.data.model

data class PokemonSpecies(
    val base_happiness: Int? = null,
    val capture_rate: Int? = null,
    val flavor_text_entries: List<PokemonFlavorTexts>? = null,
    val genera: List<PokemonGenera>? = null,
    val habitat: PokemonResults? = null,
    val evolution_chain: PokemonResults? = null
)
