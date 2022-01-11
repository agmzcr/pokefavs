package dev.agmzcr.pokefavs.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.agmzcr.pokefavs.data.model.PokemonDetails
import dev.agmzcr.pokefavs.util.DatabaseTypeConverters

@Database(entities = [PokemonDetails::class], version = 1)
@TypeConverters(DatabaseTypeConverters::class)
abstract class FavoritesDatabase: RoomDatabase() {
    abstract fun favoritesDao(): FavoritesDao
}