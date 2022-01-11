package dev.agmzcr.pokefavs.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.agmzcr.pokefavs.data.local.FavoritesDatabase
import dev.agmzcr.pokefavs.data.remote.PokeApi
import dev.agmzcr.pokefavs.data.repository.PokemonRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun providePokemonRepository(
        api: PokeApi,
        db: FavoritesDatabase
    ) = PokemonRepository(api, db)
}