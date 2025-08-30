package com.malx4c.recipesapp.di

import android.content.Context
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.malx4c.recipesapp.API_URL
import com.malx4c.recipesapp.data.RecipesRepository
import com.malx4c.recipesapp.data.database.AppDatabase
import com.malx4c.recipesapp.ui.RecipeApiService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

class AppContainer (context: Context) {
    private val contentType = "application/json".toMediaType()
    private val retrofit = Retrofit.Builder()
        .baseUrl(API_URL)
        .addConverterFactory(Json.asConverterFactory(contentType))
        .build()

    private val db = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "database-recipes"
        ).build()

    private val apiService = retrofit.create(RecipeApiService::class.java)
    private val recipesDao = db.recipesDao()
    private val categoryDao =  db.categoryDao()

    private val repository = RecipesRepository (
        recipesDao = recipesDao,
        categoryDao = categoryDao,
        recipeApiService = apiService,
    )

    val categoryListViewModelFactory = CategoryListViewModelFactory(repository)
    val favoritesViewModelFactory = FavoritesViewModelFactory(repository)
    val recipesListViewModelFactory = RecipesListViewModelFactory(repository)
    val recipeViewModelFactory = RecipeViewModelFactory(repository)
}