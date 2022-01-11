package dev.agmzcr.pokefavs.ui.details.stats

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.agmzcr.pokefavs.R
import dev.agmzcr.pokefavs.data.model.PokemonDetails
import dev.agmzcr.pokefavs.databinding.FragmentStatsBinding
import dev.agmzcr.pokefavs.ui.details.DetailsViewModel
import dev.agmzcr.pokefavs.ui.details.about.AboutFragment

@AndroidEntryPoint
class StatsFragment : Fragment(R.layout.fragment_stats) {

    companion object {
        @JvmStatic
        fun newInstance(pokemon: String?) = StatsFragment().apply {
            arguments = Bundle().apply {
                putString("pokemon", pokemon)
            }
        }
    }

    private lateinit var binding: FragmentStatsBinding
    private val statsViewModel: DetailsViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentStatsBinding.bind(view)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            vm = statsViewModel
        }
    }
}