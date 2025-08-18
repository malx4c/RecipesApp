package com.malx4c.recipesapp.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.malx4c.recipesapp.API_URL
import com.malx4c.recipesapp.model.Category
import com.malx4c.recipesapp.model.Recipe
import com.malx4c.recipesapp.ui.RecipeApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Call
import retrofit2.Retrofit

class RecipesRepository {
    private val service: RecipeApiService

    init {
        val contentType = "application/json".toMediaType()
        val retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()

        service = retrofit.create(RecipeApiService::class.java)
    }

    suspend fun getCategories(): List<Category?>? {
        return withContext(Dispatchers.IO) {
            try {
                val call: Call<List<Category?>?>? = service.getCategories()
                call?.execute()?.body()
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getRecipesByCategoryId(categoryId: Int = 0): List<Recipe?>? {
        return withContext(Dispatchers.IO) {
            try {
                val call: Call<List<Recipe?>?>? = service.getRecipes(categoryId)
                call?.execute()?.body()
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getRecipeById(recipesId: Int): Recipe? {
        return withContext(Dispatchers.IO) {
            try {
                val call: Call<Recipe?>? = service.getRecipe(recipesId)
                call?.execute()?.body()
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getRecipesByIds(recipesIds: Set<String?>?): List<Recipe?>? {
        return withContext(Dispatchers.IO) {
            try {
                val ids = recipesIds?.mapNotNull { it?.toIntOrNull() }?.joinToString(",")
                val call: Call<List<Recipe?>?>? = service.getRecipes(ids)
                call?.execute()?.body()
            } catch (e: Exception) {
                null
            }
        }
    }
}