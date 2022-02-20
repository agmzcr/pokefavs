package dev.agmzcr.pokefavs.ui.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.agmzcr.pokefavs.data.model.PokemonDetails
import dev.agmzcr.pokefavs.databinding.FavoriteListItemBinding

class FavoritesListAdapter(
    private val clickListener: ClickListener
): ListAdapter<PokemonDetails, FavoritesListAdapter.ViewHolder>(COMPARATOR) {

    interface ClickListener {
        fun onClick(pokemon: PokemonDetails)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoritesListAdapter.ViewHolder {
        val binding = FavoriteListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoritesListAdapter.ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: FavoriteListItemBinding):
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val pokemon = getItem(position)
                        if (pokemon != null) {
                            clickListener.onClick(pokemon)
                        }
                    }
                }
            }
        }

            fun bind(pokemon: PokemonDetails) {
                binding.pokemon = pokemon
                binding.executePendingBindings()
            }
        }

    /*class DiffCallback: DiffUtil.ItemCallback<PokemonDetails>() {
        override fun areItemsTheSame(oldItem: PokemonDetails, newItem: PokemonDetails): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PokemonDetails, newItem: PokemonDetails): Boolean {
            return oldItem == newItem
        }

    }*/

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<PokemonDetails>() {
            override fun areItemsTheSame(
                oldItem: PokemonDetails,
                newItem: PokemonDetails
            ) = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: PokemonDetails,
                newItem: PokemonDetails
            ) = oldItem == newItem
        }
    }

}