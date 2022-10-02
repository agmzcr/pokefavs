package dev.agmzcr.pokefavs.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import dev.agmzcr.pokefavs.R
import dev.agmzcr.pokefavs.data.repository.DataStoreManager
import dev.agmzcr.pokefavs.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var dataStoreManager: DataStoreManager
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var actionBar: ActionBar
    private lateinit var splashScreen: SplashScreen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataStoreManager = DataStoreManager(this)
        updateTheme()

        splashScreen = installSplashScreen()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        actionBar = supportActionBar!!
        actionBar.elevation = 0F
        val navHostFragment =  supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(setOf(R.id.pokedexFragment, R.id.favoritesFragment, R.id.settingsFragment))
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomNavigationView.setupWithNavController(navController)
        bottomNavigationView.elevation = 0F
        setupActionBarWithNavController(navController, appBarConfiguration)

        navigationVisibility(navController)
    }

    private fun updateTheme() {
        lifecycleScope.launch {
            dataStoreManager.getUiMode.collect {
                AppCompatDelegate.setDefaultNightMode(it)
            }
        }
    }

    private fun navigationVisibility(navController: NavController) {
        navController.addOnDestinationChangedListener {_, destination, _ ->
            when (destination.id) {
                R.id.detailsFragment -> hideBottomNavigation()
                R.id.pokedexFragment -> showBottomNavigation()
                R.id.favoritesFragment -> showBottomNavigation()
                R.id.settingsFragment -> showBottomNavigation()
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