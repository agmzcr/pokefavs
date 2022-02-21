package dev.agmzcr.pokefavs.ui.details.about

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import dev.agmzcr.pokefavs.R
import dev.agmzcr.pokefavs.data.model.PokemonDetails
import dev.agmzcr.pokefavs.databinding.FragmentAboutBinding
import dev.agmzcr.pokefavs.ui.details.DetailsViewModel

@AndroidEntryPoint
class AboutFragment : Fragment(R.layout.fragment_about) {

    companion object {
        @JvmStatic
        fun newInstance(pokemon: String?) = AboutFragment().apply {
            arguments = Bundle().apply {
                putString("pokemon", pokemon)
            }
        }
    }

    private lateinit var binding: FragmentAboutBinding
    private val viewModel: DetailsViewModel by activityViewModels()
    private val adapter = WeaknessesListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pokemon = arguments?.getString("pokemon")
        val jsonToPokemon = Gson().fromJson(pokemon, PokemonDetails::class.java)
        jsonToPokemon?.let { Log.i("TESTJson", it.toString()) }

        binding = FragmentAboutBinding.bind(view)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
        }
        setupRecyclerView()
        if (pokemon != null) {
            viewModel.getDataViewPager(jsonToPokemon)
            viewModel.pokemonDetailsData.observe(viewLifecycleOwner) {
                if (it != null) {
                    adapter.submitList(it.weaknesses)
                    Log.i("showlist", it.weaknesses.toString())
                }
            }
        } else {
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRecyclerView() {
        binding.apply {
            weaknessesRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            weaknessesRecyclerView.adapter = adapter
        }
    }
}
