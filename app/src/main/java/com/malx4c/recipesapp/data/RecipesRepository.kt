package com.malx4c.recipesapp.data

import android.content.Context
import android.util.Log
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.malx4c.recipesapp.API_URL
import com.malx4c.recipesapp.data.database.AppDatabase
import com.malx4c.recipesapp.model.Category
import com.malx4c.recipesapp.model.Recipe
import com.malx4c.recipesapp.ui.RecipeApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Call
import retrofit2.Retrofit

class RecipesRepository(context: Context) {
    private val service: RecipeApiService
    private var database: AppDatabase? = null

    init {
        val contentType = "application/json".toMediaType()
        val retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()
        service = retrofit.create(RecipeApiService::class.java)
        database = AppDatabase.getInstance(context)
    }

    suspend fun getCategories(): List<Category> {
        return withContext(Dispatchers.IO) {
            var categories: List<Category?>? = getCategoriesFromCache()
            if (categories.isNullOrEmpty()) {
                categories = getCategoriesFromBackEnd()
                if (!categories.isNullOrEmpty()) {
                    database?.categoryDao()?.insertAll(categories.filterNotNull())
                }
            }
            categories?.filterNotNull() ?: emptyList()
        }
    }

    private suspend fun getCategoriesFromBackEnd(): List<Category?>? {
        Log.d("!!! ", "getCategoriesFromBackEnd()")
        return withContext(Dispatchers.IO) {
            try {
                val call: Call<List<Category?>?>? = service.getCategories()
                call?.execute()?.body()
            } catch (e: Exception) {
                null
            }
        }
    }

    private suspend fun getCategoriesFromCache(): List<Category>? {
        Log.d("!!! ", "getCategoriesFromCache()")
        return withContext(Dispatchers.IO) {
            try {
                database?.categoryDao()?.getAll()
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getRecipesByCategoryId(categoryId: Int = 0): List<Recipe?> {
        return withContext(Dispatchers.IO) {
            var recipes: List<Recipe?>? = getRecipesByCategoryIdFromCache(categoryId)
            if (recipes.isNullOrEmpty()) {
                recipes = getRecipesByCategoryIdFromBackEnd(categoryId)
                if (!recipes.isNullOrEmpty()) {
                    recipes.map { it?.categoryId = categoryId }
                    database?.recipesDao()?.insertAll(recipes.filterNotNull())
                }
            }
            recipes?.filterNotNull() ?: emptyList()
        }
    }

    private suspend fun getRecipesByCategoryIdFromBackEnd(categoryId: Int = 0): List<Recipe?>? {
        Log.d("!!! ", "getRecipesByCategoryIdFromBackEnd($categoryId)")
        return withContext(Dispatchers.IO) {
            try {
                val call: Call<List<Recipe?>?>? = service.getRecipes(categoryId)
                call?.execute()?.body()
            } catch (e: Exception) {
                null
            }
        }
    }

    private suspend fun getRecipesByCategoryIdFromCache(categoryId: Int = 0): List<Recipe?>? {
        Log.d("!!! ", "getRecipesByCategoryIdFromCache($categoryId)")
        return withContext(Dispatchers.IO) {
            try {
                database?.recipesDao()?.getByCategoryId(categoryId)
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getRecipeById(recipeId: Int): Recipe? {
        return withContext(Dispatchers.IO) {
            var recipe: Recipe? = getRecipeByIdFromCache(recipeId)
            if (recipe == null) {
                recipe = getRecipeByIdFromBackEnd(recipeId)
                if (recipe != null) {
                    database?.recipesDao()?.insertRecipe(recipe)
                }
            }
            recipe
        }
    }

    private suspend fun getRecipeByIdFromBackEnd(recipesId: Int): Recipe? {
        Log.d("!!! ", "getRecipeByIdFromBackEnd($recipesId)")
        return withContext(Dispatchers.IO) {
            try {
                val call: Call<Recipe?>? = service.getRecipe(recipesId)
                call?.execute()?.body()
            } catch (e: Exception) {
                null
            }
        }
    }

    private suspend fun getRecipeByIdFromCache(recipesId: Int): Recipe? {
        Log.d("!!! ", "getRecipeByIdFromCache($recipesId)")
        return withContext(Dispatchers.IO) {
            try {
                database?.recipesDao()?.getById(recipesId)
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