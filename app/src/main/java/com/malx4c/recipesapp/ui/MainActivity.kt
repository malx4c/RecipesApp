package com.malx4c.recipesapp.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.malx4c.recipesapp.R
import com.malx4c.recipesapp.databinding.ActivityMainBinding
import com.malx4c.recipesapp.model.Category
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL


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

        Log.i("!!!", "Метод onCreate() выполняется на потоке: <${Thread.currentThread().name}>'")

        val thread: Thread = Thread {
            val url = URL("https://recipes.androidsprint.ru/api/category")
            val connection = url.openConnection() as HttpURLConnection
            connection.connect()

            val body = connection.inputStream.bufferedReader().readText()

            Log.i("!!!", "Выполняю запрос на потоке: <${Thread.currentThread().name}>")
            Log.i("!!!", "body: $body")

            val categories: List<Category> = Json.decodeFromString(body)

            categories.map { Log.i("!!!", "category: $it") }

        }
        thread.start()
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