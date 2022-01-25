package dev.agmzcr.pokefavs.ui.favorites

import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
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
    private val viewModel: FavoritesViewModel by viewModels()
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
            lifecycleOwner = viewLifecycleOwner
        }

        setupRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.favorites_buttoms, menu)
        val filterView = menu.findItem(R.id.short_Button)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.short_Button -> {
                filterDialog.show(childFragmentManager, "Filters")
            }
        }
        return super.onOptionsItemSelected(item)

    }

    private fun setupObserverByIds() {
        viewModel.favoritesList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            checkEmpty()
        }
    }

    private fun setupObserverByNames() {
        viewModel.favoritesListOrderByNames.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            checkEmpty()
        }
    }

    private fun checkEmpty() {
        binding.apply {
            if (adapter.itemCount == 0) {
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
       }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
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
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.favoriteRecyclerView)
        }
    }

    override fun onItemClick(pokemon: PokemonDetails) {
        val action = FavoritesFragmentDirections.actionFavoritesToDetails(
            pokemonName = pokemon.name!!
        )
        view?.findNavController()?.navigate(action)
    }

    override fun onFilter(filters: Filters) {

        if (filters.sortBy == "number") {
            setupObserverByIds()
        } else {
            setupObserverByNames()
        }

        viewModel.filters = filters
    }

    override fun onStart() {
        super.onStart()
        onFilter(viewModel.filters)
    }

}