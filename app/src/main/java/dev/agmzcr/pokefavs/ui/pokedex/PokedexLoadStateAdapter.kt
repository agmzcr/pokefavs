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
    ) {
        val progress = holder.binding.progressBar
        val txtErrorMessage = holder.binding.errorMsg
        val errorBtn = holder.binding.retryButton

        progress.isVisible = loadState is LoadState.Loading
        txtErrorMessage.isVisible = loadState is LoadState.Error
        errorBtn.isVisible = loadState is LoadState.Error

        if (loadState is LoadState.Error) {
            txtErrorMessage.text = loadState.error.localizedMessage
        }
        errorBtn.setOnClickListener {
            retry.invoke()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): StateItemViewHolder {
        return StateItemViewHolder(
            ItemNetworkStateBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    class StateItemViewHolder(
        val binding: ItemNetworkStateBinding
    ): RecyclerView.ViewHolder(binding.root) {
    }

}