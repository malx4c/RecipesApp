package com.malx4c.recipesapp.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.malx4c.recipesapp.model.Category
import com.malx4c.recipesapp.model.Recipe
import com.malx4c.recipesapp.ui.RecipeApiService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor

class RecipesRepository {
    private var _category: List<Category?>? = null
    private var _recipes: List<Recipe?>? = null
    private var _recipe: Recipe? = null
    private val service: RecipeApiService
    private val numberThreads = 10
    private val threadPool = Executors.newFixedThreadPool(numberThreads) as ThreadPoolExecutor

    val logger: HttpLoggingInterceptor = HttpLoggingInterceptor { message ->
        println("HTTP !!! $message")
    }.apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    val client = OkHttpClient.Builder()
        .addInterceptor(logger)
        .build()

    init {
        val contentType = "application/json".toMediaType()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://recipes.androidsprint.ru/api/")
            .addConverterFactory(Json.asConverterFactory(contentType))
            .client(client)
            .build()

        service = retrofit.create(RecipeApiService::class.java)
    }

    fun getCategories(callback: (List<Category?>?) -> Unit) {
        threadPool.submit {
            try {
                val call: Call<List<Category?>?>? = service.getCategories()
                _category = call?.execute()?.body()
            } catch (e: Exception) {
                _category = null
            } finally {
                callback(_category)
            }
        }
    }

    fun getRecipesByCategoryId(categoryId: Int = 0): List<Recipe?>? {
        threadPool.submit {
            val call: Call<List<Recipe?>?>? = service.getRecipes(categoryId)
            _recipes = call?.execute()?.body()
        }.get()

        return _recipes
    }

    fun getRecipeById(recipesId: Int): Recipe? {
        threadPool.submit {
            val call: Call<Recipe?>? = service.getRecipe(recipesId)
            _recipe = call?.execute()?.body()
        }.get()

        return _recipe
    }

    fun getRecipesByIds(recipesIds: Set<String?>?): List<Recipe?>? {
        val ids = recipesIds?.mapNotNull { it?.toIntOrNull() }?.joinToString(",")

        threadPool.submit {
            val call: Call<List<Recipe?>?>? = service.getRecipes(ids)
            _recipes = call?.execute()?.body()
        }.get()

        return _recipes
    }
}