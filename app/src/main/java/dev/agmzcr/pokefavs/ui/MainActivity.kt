package dev.agmzcr.pokefavs.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import dev.agmzcr.pokefavs.R
import dev.agmzcr.pokefavs.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var actionBar: ActionBar
    private lateinit var splashScreen: SplashScreen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashScreen = installSplashScreen()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        actionBar = supportActionBar!!
        actionBar.let {
            it.elevation = 0F
        }
        navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(setOf(R.id.pokedexFragment, R.id.favoritesFragment))
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomNavigationView.setupWithNavController(navController)
        bottomNavigationView.elevation = 0F
        setupActionBarWithNavController(navController, appBarConfiguration)

        navigationVisibility(navController)
    }

    private fun navigationVisibility(navController: NavController) {
        navController.addOnDestinationChangedListener {_, destination, _ ->
            when (destination.id) {
                R.id.detailsFragment -> hideBottomNavigation()
                R.id.pokedexFragment -> showBottomNavigation()
                R.id.favoritesFragment -> showBottomNavigation()
            }
        }
    }

    private fun showBottomNavigation() {
        binding.bottomNavigationView.visibility = View.VISIBLE
        actionBar.show()
    }

    private fun hideBottomNavigation() {
        binding.bottomNavigationView.visibility = View.GONE
        actionBar.hide()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }
}