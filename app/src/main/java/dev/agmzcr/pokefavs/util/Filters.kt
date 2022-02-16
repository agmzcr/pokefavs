package dev.agmzcr.pokefavs.util

import android.text.TextUtils

data class Filters(
    var sortBy: Int? = null
) {

    companion object {
        val default: Filters
            get() {
                val filters = Filters()
                filters.sortBy = 0

                return filters
            }
    }
}