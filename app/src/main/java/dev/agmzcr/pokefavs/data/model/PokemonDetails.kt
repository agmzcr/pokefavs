package dev.agmzcr.pokefavs.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "favorites")
data class PokemonDetails(
    @PrimaryKey
    var id: Int? = null,
    var name: String? = null,
    var abilities: List<PokemonAbilities>? = null,
    var stats: List<PokemonStats>? = null,
    var types: List<PokemonTypes>? = null,
    var weight: Int? = null,
    var height: Int? = null,
    var base_experience: Int? = null,
    var base_happiness: Int? = null,
    var capture_rate: Int? = null,
    var description: String? = null,
    var genera: String? = null,
    var habitat: String? = null,
    var evolutions: List<PokemonEvolutions>? =  null,
    var weaknesses: List<String>? = null
): Serializable