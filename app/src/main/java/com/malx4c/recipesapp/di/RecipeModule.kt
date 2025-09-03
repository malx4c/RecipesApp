package com.malx4c.recipesapp.di

import android.content.Context
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.malx4c.recipesapp.API_URL
import com.malx4c.recipesapp.data.dao.CategoryDao
import com.malx4c.recipesapp.data.dao.RecipesDao
import com.malx4c.recipesapp.data.database.AppDatabase
import com.malx4c.recipesapp.ui.RecipeApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class RecipeModule {

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "database-recipes"
        ).build()

    @Provides
    fun provideCategoriesDao(appDatabase: AppDatabase): CategoryDao = appDatabase.categoryDao()

    @Provides
    fun provideRecipesDao(appDatabase: AppDatabase): RecipesDao = appDatabase.recipesDao()

    @Provides
    fun provideRetrofit() : Retrofit {
        val contentType = "application/json".toMediaType()
        val retrofit = Retrofit.Builder()
        .baseUrl(API_URL)
        .addConverterFactory(Json.asConverterFactory(contentType))
        .build()
        return retrofit
    }

    @Provides
    fun provideRecipeApiService( retrofit: Retrofit): RecipeApiService {
        return retrofit.create(RecipeApiService::class.java)
    }
}