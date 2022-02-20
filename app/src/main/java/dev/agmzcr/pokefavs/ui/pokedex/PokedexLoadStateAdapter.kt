package dev.agmzcr.pokefavs.ui.pokedex

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.agmzcr.pokefavs.databinding.ItemNetworkStateBinding

class PokedexLoadStateAdapter(
    private val retry: () -> Unit
): LoadStateAdapter<PokedexLoadStateAdapter.StateItemViewHolder>() {

    override fun onBindViewHolder(
        holder: PokedexLoadStateAdapter.StateItemViewHolder,
        loadState: LoadState
    ) = holder.bind(loadState)

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState) = StateItemViewHolder(
        ItemNetworkStateBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        retry
    )

    class StateItemViewHolder(
        private val binding: ItemNetworkStateBinding,
        retry: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.retryButton.setOnClickListener { retry() }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                binding.errorMsg.text = loadState.error.localizedMessage
            }

            binding.apply {
                progressBar.isVisible = loadState is LoadState.Loading
                retryButton.isVisible = loadState is LoadState.Error
                errorMsg.isVisible = loadState is LoadState.Error
            }
        }

    }

}