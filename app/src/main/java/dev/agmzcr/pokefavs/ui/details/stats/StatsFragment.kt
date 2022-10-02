package dev.agmzcr.pokefavs.ui.details.stats

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.agmzcr.pokefavs.R
import dev.agmzcr.pokefavs.databinding.FragmentStatsBinding
import dev.agmzcr.pokefavs.ui.details.DetailsViewModel

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