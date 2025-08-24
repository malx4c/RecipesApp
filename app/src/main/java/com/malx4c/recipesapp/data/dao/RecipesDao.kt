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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(recipes: List<Recipe>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipe(recipe: Recipe)
}