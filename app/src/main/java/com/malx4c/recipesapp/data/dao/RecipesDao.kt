package com.malx4c.recipesapp.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.malx4c.recipesapp.model.Recipe

@Dao
interface RecipesDao {
    @Query("SELECT * FROM recipe WHERE categoryId = :categoryId")
    fun getByCategoryId(categoryId: Int): List<Recipe>

    @Query("SELECT * FROM recipe WHERE id = :recipeId")
    fun getById(recipeId: Int): Recipe

    @Query("SELECT * FROM recipe WHERE isFavorite = 1")
    fun getFavorites(): List<Recipe>

    @Query("UPDATE recipe SET isFavorite = NOT isFavorite WHERE id = :recipeId")
    fun setFavorites(recipeId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(recipes: List<Recipe>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipe(recipe: Recipe)
}