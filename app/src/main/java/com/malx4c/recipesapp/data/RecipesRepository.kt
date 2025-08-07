package com.malx4c.recipesapp.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.malx4c.recipesapp.model.Category
import com.malx4c.recipesapp.ui.RecipeApiService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Call
import retrofit2.Retrofit
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor

class RecipesRepository {
    private var _category: List<Category?>? = emptyList()
    private val service: RecipeApiService
    private val numberThreads = 10
    private val threadPool = Executors.newFixedThreadPool(numberThreads) as ThreadPoolExecutor

    init {
        val contentType = "application/json".toMediaType()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://recipes.androidsprint.ru/api/")
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()

        service = retrofit.create(RecipeApiService::class.java)
    }

    fun getCategories(): List<Category?>? {
        threadPool.submit {
            val call: Call<List<Category?>?>? = service.getCategories()
            _category = call?.execute()?.body()
        }.get()

        return _category
    }
}