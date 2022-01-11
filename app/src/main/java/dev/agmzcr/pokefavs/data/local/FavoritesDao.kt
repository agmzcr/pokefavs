package dev.agmzcr.pokefavs.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.agmzcr.pokefavs.data.model.PokemonDetails


@Dao
interface FavoritesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pokemon: PokemonDetails): Long

    @Query("SELECT * FROM favorites")
    fun getAllSavedPokemon(): LiveData<List<PokemonDetails>>

    @Query("SELECT * FROM favorites WHERE id LIKE :id")
    suspend fun getSavedPokemonById(id: Int) : PokemonDetails

    @Query("SELECT * FROM favorites WHERE name LIKE :name")
    suspend fun getSavedPokemonByName(name: String) : PokemonDetails

    @Query("SELECT EXISTS(SELECT * FROM favorites WHERE id LIKE :pokemonId)")
    fun isPokemonSavedById(pokemonId: Int) : Boolean

    @Query("SELECT EXISTS(SELECT * FROM favorites WHERE name LIKE :pokemonName)")
    fun isPokemonSavedByName(pokemonName: String) : Boolean

    @Query("DELETE FROM favorites WHERE id LIKE :pokemonId")
    suspend fun deletePokemon(pokemonId: Int)
}