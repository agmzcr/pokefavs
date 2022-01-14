package dev.agmzcr.pokefavs.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PokemonResults(
    val name: String? = null,
    val url: String? = null
): Parcelable
