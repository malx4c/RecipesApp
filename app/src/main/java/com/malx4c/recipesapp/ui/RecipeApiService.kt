package com.malx4c.recipesapp.ui

import com.malx4c.recipesapp.model.Category
import com.malx4c.recipesapp.model.Recipe
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApiService {
    @GET("recipe/{id}")
    fun getRecipe(@Path("id") id: Int?): Call<Recipe?>?

    @GET("recipes")
    fun getRecipes(@Query("ids") ids: String?): Call<List<Recipe?>?>?

    @GET("category/{id}")
    fun getCategory(@Path("id") id: Int?): Call<Category?>?

    @GET("category/{id}/recipes")
    fun getRecipes(@Path("id") id: Int?): Call<List<Recipe?>?>?

    @GET("category")
    fun getCategories(): Call<List<Category?>?>?
}
