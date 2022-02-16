package dev.agmzcr.pokefavs.ui.details


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import dev.agmzcr.pokefavs.R
import dev.agmzcr.pokefavs.data.model.PokemonDetails
import dev.agmzcr.pokefavs.databinding.FragmentDetailsBinding
import dev.agmzcr.pokefavs.util.UIState
import java.util.*


@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {

    private val viewModel: DetailsViewModel by viewModels()
    private lateinit var binding: FragmentDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailsBinding.bind(view)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
        }
        setupObserver()
        setupBackButton()
    }

    private fun setupObserver() {
        viewModel.isSaved.observe(viewLifecycleOwner) {
            if (it) {
                binding.isSaved = true
            }
        }
        viewModel.checkPokemon()
        viewModel.state.observe(viewLifecycleOwner) { data ->
            when(data) {
                is UIState.Loading -> {}
                is UIState.Success<*> -> {
                    data.let {
                        if (it.content != null) {
                            val pokemonToJson = Gson().toJson(it.content)
                            binding.pokemon = it.content as PokemonDetails
                            setupViewPager(pokemonToJson)
                            setupFavoriteButton(it.content)
                        }
                    }
                }
                is UIState.Error -> {
                    Toast.makeText(context, data.errorMessage, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }

    private fun setupFavoriteButton(pokemon: PokemonDetails) {
        binding.favoriteButton.setOnClickListener {
            if (!binding.isSaved) {
                pokemon.let { pokeData ->
                    viewModel.insertPokemon(pokeData)
                    Toast.makeText(context, "${pokeData.name?.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(
                            Locale.getDefault()
                        ) else it.toString()
                    }} is saved!", Toast.LENGTH_SHORT).show()
                }
                binding.isSaved = true
            } else {
                pokemon.let { pokeData ->
                    viewModel.deletePokemon(pokeData.id!!)
                    Toast.makeText(context, "${pokeData.name?.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(
                            Locale.getDefault()
                        ) else it.toString()
                    }} is deleted", Toast.LENGTH_SHORT).show()
                }
                binding.isSaved = false
            }
        }
    }

    private fun setupBackButton() {
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupViewPager(pokemonJson: String) {
        val viewPager: ViewPager2 = binding.viewPager
        val viewPagerAdapter = ViewPagerAdapter(requireContext(), childFragmentManager, lifecycle, pokemonJson)
        viewPager.adapter = viewPagerAdapter
        val tabs: TabLayout = binding.tabsLayout
        TabLayoutMediator(tabs, viewPager){tab, position ->
            tab.text = viewPagerAdapter.pages[position].title
        }.attach()
    }
}