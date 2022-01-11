package dev.agmzcr.pokefavs.data.model

data class PokemonSpecies(
    val base_happiness: Int,
    val capture_rate: Int,
    val flavor_text_entries: List<PokemonFlavorTexts>,
    val genera: List<PokemonGenera>,
    val habitat: PokemonResults,
    val evolution_chain: PokemonResults?
)
