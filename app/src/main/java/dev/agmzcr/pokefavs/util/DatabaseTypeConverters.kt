package dev.agmzcr.pokefavs.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import dev.agmzcr.pokefavs.data.model.*

class DatabaseTypeConverters {

    @TypeConverter
    fun listToJson(value: List<*>) = Gson().toJson(value)

    @TypeConverter
    fun evolutionsJsonToList(value: String)  =
        Gson().fromJson(value, Array<PokemonEvolutions>::class.java).toList()

    @TypeConverter
    fun statsJsonToList(value: String) =
        Gson().fromJson(value, Array<PokemonStats>::class.java).toList()

    @TypeConverter
    fun typesJsonToList(value: String) =
        Gson().fromJson(value, Array<PokemonTypes>::class.java).toList()

    @TypeConverter
    fun ablitiesJsonToList(value: String) =
        Gson().fromJson(value, Array<PokemonAbilities>::class.java).toList()

    @TypeConverter
    fun weaknessesJsonToList(value: String) =
        Gson().fromJson(value, Array<String>::class.java).toList()

}
