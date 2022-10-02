package dev.agmzcr.pokefavs.ui.favorites

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.agmzcr.pokefavs.R
import dev.agmzcr.pokefavs.data.model.PokemonDetails
import dev.agmzcr.pokefavs.databinding.FragmentFavoritesBinding
import dev.agmzcr.pokefavs.util.Filters

@AndroidEntryPoint
class FavoritesFragment : Fragment(R.layout.fragment_favorites),
    FavoritesListAdapter.ClickListener,
    FilterDialogFragment.FilterListener {

    private lateinit var binding: FragmentFavoritesBinding
    private val adapter =  FavoritesListAdapter(this)
    private val viewModel: FavoritesViewModel by activityViewModels()
    private lateinit var filterDialog: FilterDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavoritesBinding.bind(view)
        filterDialog = FilterDialogFragment()
        binding.apply {
            vm = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        setupRecyclerView()
        getList()
    }

    private fun setupObserver(orderBy: String) {
        viewModel.favorites(orderBy).observe(viewLifecycleOwner) { list ->
            if(list.isNullOrEmpty()) {
                checkEmpty(true)
            } else {
                checkEmpty(false)
                adapter.submitList(list)
            }
        }
    }

    private fun getList() {
            viewModel.getFilters().observe(viewLifecycleOwner) { filter ->
                if (filter != null) {
                    setupObserver(filter.sortBy!!)
                }
            }
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.favorites_buttoms, menu)
        //val filterView = menu.findItem(R.id.short_Button)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.short_Button -> {
                filterDialog.show(childFragmentManager, "Filters")
            }
        }
        return super.onOptionsItemSelected(item)

    }

    private fun checkEmpty(empty: Boolean) {
        binding.apply {
            if (empty) {
                imageEmpty.visibility = View.VISIBLE
                textEmpty.visibility = View.VISIBLE
            } else {
                imageEmpty.visibility = View.GONE
                textEmpty.visibility = View.GONE
            }
        }
    }

    private fun setupRecyclerView() {
       binding.apply {
           favoriteRecyclerView.layoutManager = LinearLayoutManager(context)
           favoriteRecyclerView.adapter = adapter
           favoriteRecyclerView.setHasFixedSize(true)
       }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.absoluteAdapterPosition
                val pokemon = adapter.currentList[position]
                viewModel.deletePokemon(pokemon.id!!)

                view?.let {
                    Snackbar.make(it, "Pokemon deleted", Snackbar.LENGTH_LONG).apply {
                        setAction("Undo") {
                            viewModel.undoDeletion(pokemon)
                        }.show()
                    }
                }
                getList()
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.favoriteRecyclerView)
        }
    }

    override fun onClick(pokemon: PokemonDetails) {
        val action = FavoritesFragmentDirections.actionFavoritesToDetails(
            pokemonName = pokemon.name!!
        )
        view?.findNavController()?.navigate(action)
    }

    override fun onFilter(filters: Filters) {
        setupObserver(filters.sortBy!!)

        viewModel.setFilters(filters)
    }
}