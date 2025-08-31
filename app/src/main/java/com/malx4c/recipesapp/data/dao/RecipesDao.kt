package com.malx4c.recipesapp.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.malx4c.recipesapp.model.Recipe

@Dao
interface RecipesDao {
    @Query("SELECT * FROM recipe WHERE categoryId = :categoryId")
    suspend fun getByCategoryId(categoryId: Int): List<Recipe>

    @Query("SELECT * FROM recipe WHERE id = :recipeId")
    suspend fun getById(recipeId: Int): Recipe

    @Query("SELECT * FROM recipe WHERE isFavorite = 1")
    suspend fun getFavorites(): List<Recipe>

    @Query("UPDATE recipe SET isFavorite = NOT isFavorite WHERE id = :recipeId")
    suspend fun setFavorites(recipeId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(recipes: List<Recipe>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: Recipe)
}