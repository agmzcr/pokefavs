package dev.agmzcr.pokefavs.ui.favorites

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import dev.agmzcr.pokefavs.R
import dev.agmzcr.pokefavs.databinding.FragmentFilterDialogBinding
import dev.agmzcr.pokefavs.util.Filters

class FilterDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentFilterDialogBinding
    private var filterListener: FilterListener? = null

    interface FilterListener {
        fun onFilter(filters: Filters)
    }

    private val selectedSortBy: String?
        get() {
            val selected = binding.spinnerSort.selectedItem as String
            if (getString(R.string.sort_by_number) == selected) {
                Log.i("selected1", "selected por valoracion")
                return "number"
            }
            return if (getString(R.string.sort_by_name) == selected) {
                Log.i("selected1", "selected por popular")
                "name"
            } else {
                null
            }
        }

    val filters: Filters
        get() {
            val filters = Filters()

            filters.sortBy = selectedSortBy

            return filters
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFilterDialogBinding.inflate(inflater, container, false)

        binding.buttonSearch.setOnClickListener { onSearchClicked() }
        binding.buttonCancel.setOnClickListener { onCancelClicked() }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (parentFragment is FilterListener) {
            filterListener = parentFragment as FilterListener
        }
    }

    private fun onSearchClicked() {
        filterListener?.onFilter(filters)
        dismiss()
    }

    private fun onCancelClicked() {
        dismiss()
    }

}