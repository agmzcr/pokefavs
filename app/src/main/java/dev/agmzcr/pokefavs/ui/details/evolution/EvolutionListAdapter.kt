package dev.agmzcr.pokefavs.ui.details.evolution

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.agmzcr.pokefavs.data.model.PokemonEvolutions
import dev.agmzcr.pokefavs.databinding.EvolutionListItemBinding

class EvolutionListAdapter(

): ListAdapter<PokemonEvolutions, EvolutionListAdapter.ViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EvolutionListAdapter.ViewHolder {
        val binding = EvolutionListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EvolutionListAdapter.ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: EvolutionListItemBinding):
            RecyclerView.ViewHolder(binding.root) {

        fun bind(pokemon: PokemonEvolutions) {
            binding.pokemon = pokemon
            binding.executePendingBindings()
        }
    }

    class DiffCallback: DiffUtil.ItemCallback<PokemonEvolutions>() {
        override fun areItemsTheSame(oldItem: PokemonEvolutions, newItem: PokemonEvolutions): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: PokemonEvolutions, newItem: PokemonEvolutions): Boolean {
            return oldItem == newItem
        }

    }

}