package dev.agmzcr.pokefavs.util

import android.text.TextUtils

class Filters {

    companion object {

        val default: Filters
            get() {
                val filters = Filters()
                filters.sortBy = "number"

                return filters
            }
    }

    var sortBy: String? = null

    fun hasSortBy(): Boolean {
        return !TextUtils.isEmpty(sortBy)
    }
}