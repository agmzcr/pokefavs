package dev.agmzcr.pokefavs.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "favorites")
data class PokemonDetails(
    @PrimaryKey
    val id: Int? = null,
    val name: String? = null,
    val abilities: List<PokemonAbilities>? = null,
    val stats: List<PokemonStats>? = null,
    val types: List<PokemonTypes>? = null,
    val weight: Int? = null,
    val height: Int? = null,
    val base_experience: Int? = null,
    var base_happiness: Int? = null,
    var capture_rate: Int? = null,
    var description: String? = null,
    var genera: String? = null,
    var habitat: String? = null,
    var evolutions: List<PokemonEvolutions>? =  null,
) : Serializable