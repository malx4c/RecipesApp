package com.malx4c.recipesapp.data

import android.util.Log
import com.malx4c.recipesapp.data.dao.CategoryDao
import com.malx4c.recipesapp.data.dao.RecipesDao
import com.malx4c.recipesapp.model.Category
import com.malx4c.recipesapp.model.Recipe
import com.malx4c.recipesapp.ui.RecipeApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import javax.inject.Inject

class RecipesRepository @Inject constructor(
    private val recipesDao: RecipesDao,
    private val categoryDao: CategoryDao,
    private val recipeApiService: RecipeApiService,
) {

    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    suspend fun getCategories(): List<Category> {
        return withContext(ioDispatcher) {
            var categories: List<Category?>? = getCategoriesFromCache()
            if (categories.isNullOrEmpty()) {
                categories = getCategoriesFromBackEnd()
                if (!categories.isNullOrEmpty()) {
                    categoryDao.insertAll(categories.filterNotNull())
                }
            }
            categories?.filterNotNull() ?: emptyList()
        }
    }

    private suspend fun getCategoriesFromBackEnd(): List<Category?>? {
        return withContext(ioDispatcher) {
            try {
                val call: Call<List<Category?>?>? = recipeApiService.getCategories()
                call?.execute()?.body()
            } catch (e: Exception) {
                null
            }
        }
    }

    private suspend fun getCategoriesFromCache(): List<Category>? {
        return withContext(ioDispatcher) {
            try {
                categoryDao.getAll()
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getRecipesByCategoryId(categoryId: Int = 0): List<Recipe?> {
        return withContext(ioDispatcher) {
            var recipes: List<Recipe?>? = getRecipesByCategoryIdFromCache(categoryId)
            if (recipes.isNullOrEmpty()) {
                recipes = getRecipesByCategoryIdFromBackEnd(categoryId)
                if (!recipes.isNullOrEmpty()) {
                    recipes.map { it?.categoryId = categoryId }
                    recipesDao.insertAll(recipes.filterNotNull())
                }
            }
            recipes?.filterNotNull() ?: emptyList()
        }
    }

    private suspend fun getRecipesByCategoryIdFromBackEnd(categoryId: Int = 0): List<Recipe?>? {
        return withContext(ioDispatcher) {
            try {
                val call: Call<List<Recipe?>?>? = recipeApiService.getRecipes(categoryId)
                call?.execute()?.body()
            } catch (e: Exception) {
                null
            }
        }
    }

    private suspend fun getRecipesByCategoryIdFromCache(categoryId: Int = 0): List<Recipe?>? {
        return withContext(ioDispatcher) {
            try {
                recipesDao.getByCategoryId(categoryId)
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getRecipeById(recipeId: Int): Recipe? {
        return withContext(ioDispatcher) {
            var recipe: Recipe? = getRecipeByIdFromCache(recipeId)
            if (recipe == null) {
                recipe = getRecipeByIdFromBackEnd(recipeId)
                if (recipe != null) {
                    recipesDao.insertRecipe(recipe)
                }
            }
            recipe
        }
    }

    private suspend fun getRecipeByIdFromBackEnd(recipesId: Int): Recipe? {
        return withContext(ioDispatcher) {
            try {
                val call: Call<Recipe?>? = recipeApiService.getRecipe(recipesId)
                call?.execute()?.body()
            } catch (e: Exception) {
                null
            }
        }
    }

    private suspend fun getRecipeByIdFromCache(recipesId: Int): Recipe? {
        return withContext(ioDispatcher) {
            try {
                recipesDao.getById(recipesId)
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun setFavorite(recipesId: Int?) {
        withContext(ioDispatcher) {
            try {
                recipesId?.let { recipesDao.setFavorites(it) }
            } catch (e: Exception) {
                Log.e("setFavorite", "Error setting favorite for recipe ID: $recipesId", e)
            }
        }
    }

    suspend fun getFavorites(): List<Recipe?>? {
        return withContext(ioDispatcher) {
            try {
                recipesDao.getFavorites()
            } catch (e: Exception) {
                null
            }
        }
    }
}