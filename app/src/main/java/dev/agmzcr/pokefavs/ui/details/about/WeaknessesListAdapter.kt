package dev.agmzcr.pokefavs.ui.details.about

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.agmzcr.pokefavs.databinding.TypeListItemBinding

class WeaknessesListAdapter: ListAdapter<String, WeaknessesListAdapter.ViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WeaknessesListAdapter.ViewHolder {
        val binding = TypeListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeaknessesListAdapter.ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: TypeListItemBinding):
            RecyclerView.ViewHolder(binding.root) {
                fun bind(type: String) {
                    binding.type = type
                    binding.executePendingBindings()
                }
            }

    class DiffCallback: DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }
}