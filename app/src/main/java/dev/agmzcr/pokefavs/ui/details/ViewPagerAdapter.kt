package dev.agmzcr.pokefavs.ui.details

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import dev.agmzcr.pokefavs.data.model.PokemonDetails
import dev.agmzcr.pokefavs.ui.details.about.AboutFragment
import dev.agmzcr.pokefavs.ui.details.evolution.EvolutionFragment
import dev.agmzcr.pokefavs.ui.details.stats.StatsFragment

class ViewPagerAdapter(
    private val context: Context,
    fragmentManager: FragmentManager,
    private val pokemon: String
) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    data class Page(val title: String, val ctor: () -> Fragment)

    override fun getCount() = 3

    @Suppress("MoveLambdaOutsideParentheses")
    private val pages = listOf(
        Page(
            "About",
            { AboutFragment.newInstance(pokemon) }
        ),
        Page(
            "Stats",
            { StatsFragment.newInstance(pokemon) }
        ),
        Page(
            "Evolution",
            { EvolutionFragment.newInstance(pokemon) }
        )
    )

    override fun getItem(position: Int): Fragment {
        return pages[position].ctor()
    }

    override fun getPageTitle(position: Int): CharSequence = when(position) {
        0 -> "About"
        1 -> "Stats"
        2 -> "Evolution"
        else -> throw IllegalStateException("Unexpected position $position")
    }
}