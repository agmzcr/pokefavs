package dev.agmzcr.pokefavs.ui.details.evolution

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.agmzcr.pokefavs.R
import dev.agmzcr.pokefavs.databinding.FragmentEvolutionBinding
import dev.agmzcr.pokefavs.ui.details.DetailsViewModel


@AndroidEntryPoint
class EvolutionFragment : Fragment(R.layout.fragment_evolution) {

    companion object {
        @JvmStatic
        fun newInstance(pokemon: String?) = EvolutionFragment().apply {
            arguments = Bundle().apply {
                putString("pokemon", pokemon)
            }
        }
    }

    private lateinit var binding: FragmentEvolutionBinding
    private val viewModel: DetailsViewModel by activityViewModels()
    private val adapter = EvolutionListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEvolutionBinding.bind(view)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
        }
        setupRecyclerView()
        viewModel.pokemonDetailsData.observe(viewLifecycleOwner) {
            if (it != null) {
                adapter.submitList(it.evolutions)
            }
        }
    }

    private fun setupRecyclerView() {
        binding.apply {
            evolutionRecyclerView.layoutManager = LinearLayoutManager(context)
            evolutionRecyclerView.adapter = adapter
        }
    }

}