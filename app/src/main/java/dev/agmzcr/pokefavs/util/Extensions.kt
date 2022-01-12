package dev.agmzcr.pokefavs.util

import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.palette.graphics.Palette
import dev.agmzcr.pokefavs.R

fun String.asResourceId(): Int {
    return when (this) {
        "bug" -> R.drawable.ic_bug_type_icon
        "dark" -> R.drawable.ic_dark_type_icon
        "dragon" -> R.drawable.ic_dragon_type_icon
        "electric" -> R.drawable.ic_electric_type_icon
        "fairy" -> R.drawable.ic_fairy_type_icon
        "fighting" -> R.drawable.ic_fighting_type_icon
        "fire" -> R.drawable.ic_fire_type_icon
        "flying" -> R.drawable.ic_flying_type_icon
        "grass" -> R.drawable.ic_grass_type_icon
        "ground" -> R.drawable.ic_ground_type_icon
        "ice" -> R.drawable.ic_ice_type_icon
        "normal" -> R.drawable.ic_normal_type_icon
        "poison" -> R.drawable.ic_poison_type_icon
        "psychic" -> R.drawable.ic_psychic_type_icon
        "rock" -> R.drawable.ic_rock_type_icon
        "steel" -> R.drawable.ic_steel_type_icon
        "water" -> R.drawable.ic_water_type_icon
        "ghost" -> R.drawable.ic_ghost_type_icon
        else -> R.drawable.ic_logo
    }
}

fun String.extractPokemonIdFromUrl(): Int =
    this.substringAfter("pokemon").replace("/", "").toInt()

fun String.extractPokemonIdFromUrlSpecies(): Int =
    this.substringAfter("pokemon-species").replace("/", "").toInt()

fun String.extractPokemonIdFromUrlEvolution(): Int =
    this.substringAfter("evolution-chain").replace("/", "").toInt()