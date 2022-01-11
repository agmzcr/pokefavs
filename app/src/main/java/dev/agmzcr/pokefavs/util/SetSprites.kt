package dev.agmzcr.pokefavs.util

fun getSpriteFromUrlPokemon(url: String): String {
    val pokeId = url
        .replace("https://pokeapi.co/api/v2/pokemon/", "")
        .replace("/", "")

    return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokeId}.png"
}

fun getSpriteFromUrlSpecies(url: String): String {
    val pokeId = url
        .replace("https://pokeapi.co/api/v2/pokemon-species/", "")
        .replace("/", "")

    return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokeId}.png"
}

fun getSpriteFromId(id: Int) : String {
    val idString = id.toString()

    return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${idString}.png"
}