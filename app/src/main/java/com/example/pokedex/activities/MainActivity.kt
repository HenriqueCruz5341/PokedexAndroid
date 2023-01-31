package com.example.pokedex.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.pokedex.R
import com.example.pokedex.databinding.ActivityMainBinding

/**
 * Main activity of the app.
 *
 * It contains the bottom navigation view.
 * It also contains the navigation graph.
 * The navigation graph contains the fragments that are displayed in the main activity.
 * The navigation graph is used to navigate between the fragments.
 * The navigation graph is also used to pass data between the fragments.
 *
 * @property binding The binding object of the activity.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val navHostFrag = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFrag.navController
        binding.navView.setupWithNavController(navController)
    }
}