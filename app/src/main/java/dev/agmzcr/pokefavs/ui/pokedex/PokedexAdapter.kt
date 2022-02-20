package dev.agmzcr.pokefavs.ui.pokedex

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.agmzcr.pokefavs.data.model.PokemonResults
import dev.agmzcr.pokefavs.databinding.PokedexListItemBinding

class PokedexAdapter(
    private val clickListener: OnClickListener
) : PagingDataAdapter<PokemonResults, PokedexAdapter.ViewHolder>(COMPARATOR) {

    interface OnClickListener {
        fun onClick(pokemon: PokemonResults)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PokedexAdapter.ViewHolder {
        val binding =
            PokedexListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokedexAdapter.ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount) {
            2
        } else {
            1
        }
    }

    inner class ViewHolder(private val binding: PokedexListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val pokemon = getItem(position)
                    if (pokemon != null) {
                        clickListener.onClick(pokemon)
                    }
                }
            }
        }

            fun bind(pokemon: PokemonResults) {
                binding.pokemon = pokemon
                binding.executePendingBindings()
            }
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<PokemonResults>() {
            override fun areItemsTheSame(oldItem: PokemonResults, newItem: PokemonResults) =
                oldItem.url == newItem.url

            override fun areContentsTheSame(oldItem: PokemonResults, newItem: PokemonResults) =
                oldItem == newItem

        }
    }
}