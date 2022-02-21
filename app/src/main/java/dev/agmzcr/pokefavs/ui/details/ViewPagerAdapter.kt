package dev.agmzcr.pokefavs.ui.details

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import dev.agmzcr.pokefavs.ui.details.about.AboutFragment
import dev.agmzcr.pokefavs.ui.details.evolution.EvolutionFragment
import dev.agmzcr.pokefavs.ui.details.stats.StatsFragment

class ViewPagerAdapter(
    private val context: Context,
    fm: FragmentManager,
    lifecycle: Lifecycle,
    private val pokemon: String
): FragmentStateAdapter(fm, lifecycle) {

    data class Page(val title: String, val ctor: () -> Fragment)

    override fun getItemCount() = 3

    val pages = listOf(
        Page(
            "About"
        ) { AboutFragment.newInstance(pokemon) },
        Page(
            "Stats"
        ) { StatsFragment.newInstance(pokemon) },
        Page(
            "Evolutions"
        ) { EvolutionFragment.newInstance(pokemon) }
    )

    override fun createFragment(position: Int): Fragment {
        return pages[position].ctor()
    }
}