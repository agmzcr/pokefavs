package dev.agmzcr.pokefavs.ui.favorites

import android.app.Dialog
import android.os.Bundle
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

@AndroidEntryPoint
class FavoritesFragment : Fragment(R.layout.fragment_favorites), FavoritesListAdapter.ClickListener {

    private lateinit var binding: FragmentFavoritesBinding
    private val adapter =  FavoritesListAdapter(this)
    private val viewModel: FavoritesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavoritesBinding.bind(view)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
        }

        setupRecyclerView()
        setupObserver()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.favorites_buttoms, menu)
        val filterView = menu.findItem(R.id.short_Button)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.short_Button -> {
                val dialog = BottomSheetDialog(context!!)
                val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog, null)
                dialog.setCancelable(true)
                dialog.setContentView(view)
                dialog.show()
                val btnClose = view.findViewById<Button>(R.id.idBtnDismiss)
                val doneAlpha = view.findViewById<ImageView>(R.id.doneAlphabetically)
                val doneId = view.findViewById<ImageView>(R.id.doneId)
                val textAlpha = view.findViewById<TextView>(R.id.alphaTextDialog)
                val textId = view.findViewById<TextView>(R.id.idTextDialog)
                textId.setOnClickListener {
                    doneId.visibility = View.VISIBLE
                    doneAlpha.visibility = View.INVISIBLE
                }

                textAlpha.setOnClickListener {
                    doneAlpha.visibility = View.VISIBLE
                    doneId.visibility = View.INVISIBLE
                }
                btnClose.setOnClickListener {
                    if (doneId.visibility == View.VISIBLE) {
                        val filterList = adapter.currentList.sortedBy { it.id }
                        adapter.submitList(filterList)
                    }else {
                        val filterList = adapter.currentList.sortedBy { it.name }
                        adapter.submitList(filterList)
                    }
                    dialog.dismiss()
                }
            }
        }
        return super.onOptionsItemSelected(item)

    }

    private fun setupObserver() {
        viewModel.favoritesList.observe(viewLifecycleOwner) {
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

}