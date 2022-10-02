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
    suspend fun insert(pokemon: PokemonDetails)

    @Query("DELETE FROM favorites WHERE id LIKE :pokemonId")
    suspend fun delete(pokemonId: Int)

    @Query("SELECT * FROM favorites ORDER BY id")
    fun getAllSavedPokemonByIds(): LiveData<List<PokemonDetails>>

    @Query("SELECT * FROM favorites ORDER BY name")
    fun getAllSavedPokemonOrderByNames(): LiveData<List<PokemonDetails>>

    @Query("SELECT * FROM favorites WHERE name LIKE :name")
    fun getSavedPokemonByName(name: String): PokemonDetails

}