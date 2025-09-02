package com.malx4c.recipesapp.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.malx4c.recipesapp.R
import com.malx4c.recipesapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("ActivityMainBinding is null")

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnCategories.setOnClickListener {
            showFragment(R.id.categoriesListFragment)
        }

        binding.btnFavorites.setOnClickListener {
            showFragment(R.id.favoritesFragment)
        }
    }

    private fun showFragment(fragmentId: Int) {
        val navController = findNavController(R.id.nav_host_fragment)
        val activeFragmentId = navController.currentDestination?.id
        if (activeFragmentId == fragmentId) return

        val navOptions = NavOptions.Builder()
            .setEnterAnim(R.anim.fade_in)
            .build()
        navController.navigate(fragmentId, null, navOptions)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}