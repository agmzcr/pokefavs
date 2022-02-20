package dev.agmzcr.pokefavs.ui.pokedex

import androidx.paging.PagingSource
import androidx.paging.PagingState
import dev.agmzcr.pokefavs.data.model.PokemonResults
import dev.agmzcr.pokefavs.data.remote.PokeApi
import dev.agmzcr.pokefavs.util.extractPokemonIdFromUrl
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception

class PokedexPagingSource(
    private val api: PokeApi,
    private val query: String? = null
): PagingSource<Int, PokemonResults>() {

    override fun getRefreshKey(state: PagingState<Int, PokemonResults>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PokemonResults> {
        val offsetValue = params.key ?: 0
        val loadSize = if (query == null) params.loadSize else 100

        return try {
            val response = api.getAllPokemonList(loadSize, offsetValue)
            val filteredData = if (query != null) {
                response.results.filter {it.name!!.contains(query, true)}
            } else {
                response.results
            }

            LoadResult.Page(
                data = filteredData,
                prevKey = if (offsetValue == 0) null else  offsetValue - loadSize,
                nextKey = offsetValue + loadSize
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}