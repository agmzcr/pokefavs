package dev.agmzcr.pokefavs.ui.pokedex

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import dev.agmzcr.pokefavs.R
import dev.agmzcr.pokefavs.data.model.PokemonResults
import dev.agmzcr.pokefavs.databinding.FragmentPokedexBinding
import dev.agmzcr.pokefavs.util.CheckConnection
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PokedexFragment : Fragment(R.layout.fragment_pokedex), PokedexAdapter.OnClickListener {

    private val viewModel: PokedexViewModel by viewModels()
    private val adapter = PokedexAdapter(this)
    private lateinit var binding: FragmentPokedexBinding
    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    // This livedata help to iniciate automatically start the fetch if it detects
    // that the connection is ready
    private fun checkNetworkLiveData() {
        val connectionLiveData = CheckConnection(requireContext())
        connectionLiveData.observe(viewLifecycleOwner) { isConnected ->
            if (isConnected) {
                setupFetchingPokemons(null)
                binding.errorText.visibility = View.GONE
            } else {
                binding.errorText.visibility = View.VISIBLE
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPokedexBinding.bind(view)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
        }
        checkNetworkLiveData()
        setupRecyclerView()
        setupRefresh()
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_buttom, menu)
        val searchItem = menu.findItem(R.id.search_Button)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                setupFetchingPokemons(query!!)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        searchView.setOnCloseListener {
            setupFetchingPokemons(null)
            false
        }
    }

    private fun setupFetchingPokemons(query: String?) {
        job?.cancel()
        job = lifecycleScope.launch {
            viewModel.getPokemonList(query)?.collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun setupRefresh() {
        binding.apply {
            swipeRefresh.setOnRefreshListener {
                adapter.refresh()
                swipeRefresh.isRefreshing = false
            }
        }
    }

    private fun setupRecyclerView() {
        val gridLayoutManager = GridLayoutManager(requireContext(), 3)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = adapter.getItemViewType(position)
                return if (viewType == 1) 1 else 2
            }
        }
        binding.apply {
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
            recyclerView.adapter = adapter.withLoadStateFooter(
                footer = PokedexLoadStateAdapter { retry() }
            )
            adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }
        adapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading &&
                adapter.snapshot().isEmpty()) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
                val errorState = when {
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                errorState?.let {
                    Log.d("LoadStateError", it.error.toString())
                }
            }
        }
    }

    override fun onClick(pokemon: PokemonResults) {
        val action = PokedexFragmentDirections.actionPokedexToDetails(
            pokemonName = pokemon.name
        )
        view?.findNavController()?.navigate(action)
    }

    private fun retry() {
        adapter.retry()
    }
}