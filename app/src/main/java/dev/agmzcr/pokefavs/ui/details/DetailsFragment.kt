package dev.agmzcr.pokefavs.ui.details


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import dev.agmzcr.pokefavs.R
import dev.agmzcr.pokefavs.databinding.FragmentDetailsBinding


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
            viewmodel = viewModel
        }
        setupObserver()
        setupBackButton()
        setupFavoriteButton()
    }

    private fun setupObserver() {
        viewModel.isSaved.observe(this, {
            if (it) {
                binding.isSaved = true
            }
        })
        viewModel.checkPokemon()
        viewModel.pokemonDetailsDataToSend.observe(this, { data ->
            data?.let {
                val pokemonToJson = Gson().toJson(it)
                Log.i("pokemonJson2", pokemonToJson)

                setupPager(pokemonToJson)
            }
        })
    }

    private fun setupFavoriteButton() {
        binding.favoriteButton.setOnClickListener {
            if (!binding.isSaved) {
                viewModel.pokemonDetailsData.value?.let {
                    viewModel.insertPokemon(it)
                    Toast.makeText(context, "${it.name?.capitalize()} is saved!", Toast.LENGTH_SHORT).show()
                }
                binding.isSaved = true
            } else {
                viewModel.pokemonDetailsData.value?.let {
                    viewModel.deletePokemon(it.id!!)
                    Toast.makeText(context, "${it.name?.capitalize()} is deleted", Toast.LENGTH_SHORT).show()
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

    private fun setupPager(pokemonJson: String) {
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = ViewPagerAdapter(requireContext(), childFragmentManager, pokemonJson)
        binding.tabsLayout.setupWithViewPager(viewPager)
    }
}