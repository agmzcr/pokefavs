package dev.agmzcr.pokefavs.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.agmzcr.pokefavs.data.model.PokemonDetails
import kotlinx.coroutines.flow.Flow


@Dao
interface FavoritesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pokemon: PokemonDetails): Long

    @Query("DELETE FROM favorites WHERE id LIKE :pokemonId")
    suspend fun delete(pokemonId: Int)

    @Query("SELECT * FROM favorites ORDER BY id")
    fun getAllSavedPokemonByIds(): Flow<List<PokemonDetails>>

    @Query("SELECT * FROM favorites ORDER BY name")
    fun getAllSavedPokemonOrderByNames(): Flow<List<PokemonDetails>>

    @Query("SELECT * FROM favorites WHERE id LIKE :id")
    fun getSavedPokemonById(id: Int): Flow<PokemonDetails>

    @Query("SELECT * FROM favorites WHERE name LIKE :name")
    fun getSavedPokemonByName(name: String): PokemonDetails

    @Query("SELECT EXISTS(SELECT * FROM favorites WHERE id LIKE :pokemonId)")
    fun isPokemonSavedById(pokemonId: Int) : Boolean

    @Query("SELECT EXISTS(SELECT * FROM favorites WHERE name LIKE :pokemonName)")
    fun isPokemonSavedByName(pokemonName: String) : Boolean

}