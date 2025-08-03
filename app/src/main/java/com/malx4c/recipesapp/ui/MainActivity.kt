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
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor


class MainActivity : AppCompatActivity() {
    private val numberThreads = 10
    private val threadPool = Executors.newFixedThreadPool(numberThreads) as ThreadPoolExecutor

    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("ActivityMainBinding is null")

    override fun onCreate(savedInstanceState: Bundle?) {
        var categories: List<Category>
        var categoryIds: List<Int> = emptyList()
        val logger: HttpLoggingInterceptor = HttpLoggingInterceptor { message ->
            println("HTTP !!! $message")
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()

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

        Log.i("!!!", "\tМетод onCreate() выполняется на потоке: <${Thread.currentThread().name}>'")

        val thread = Thread {
            threadPool.submit {
                val request = Request.Builder()
                    .url("https://recipes.androidsprint.ru/api/category")
                    .build()
                val response = client.newCall(request).execute()
                val body = response.body?.string() ?: ""

                Log.i("!!!", "\t->Выполняю запрос на потоке: <${Thread.currentThread().name}>")
                Log.i("!!!", "body: $body")

                categories = Json.decodeFromString(body)
                categories.map { Log.i("!!!", "category: $it") }
                categoryIds = categories.map { it.id }
            }.get()

            categoryIds.map {
                val requestRecipes = Request.Builder()
                    .url("https://recipes.androidsprint.ru/api/category/$it/recipes")
                    .build()

                threadPool.submit {
                    Log.i(
                        "!!!",
                        "\tВыполняю запрос на потоке: <${Thread.currentThread().name}>"
                    )
                    val responseRecipes = client.newCall(requestRecipes).execute()
                    val body = responseRecipes.body?.string() ?: ""
                    Log.i("!!!", body)
                }
            }
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
        threadPool.shutdown()
    }
}