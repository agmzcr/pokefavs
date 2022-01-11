package dev.agmzcr.pokefavs.data.model

data class PokemonDamage(
    val double_damage_from: List<PokemonResults>,
    val double_damage_to: List<PokemonResults>,
    val half_damage_from: List<PokemonResults>,
    val half_damage_to: List<PokemonResults>,
    val no_damage_from: List<PokemonResults>,
    val no_damage_to: List<PokemonResults>
)