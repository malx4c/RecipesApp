package com.malx4c.recipesapp.data

import com.malx4c.recipesapp.data.dao.CategoryDao
import com.malx4c.recipesapp.data.dao.RecipesDao
import com.malx4c.recipesapp.model.Category
import com.malx4c.recipesapp.model.Recipe
import com.malx4c.recipesapp.ui.RecipeApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call

class RecipesRepository(
    private val recipesDao: RecipesDao,
    private val categoryDao: CategoryDao,
    private val recipeApiService: RecipeApiService,
) {


    suspend fun getCategories(): List<Category> {
        return withContext(Dispatchers.IO) {
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
        return withContext(Dispatchers.IO) {
            try {
                val call: Call<List<Category?>?>? = recipeApiService.getCategories()
                call?.execute()?.body()
            } catch (e: Exception) {
                null
            }
        }
    }

    private suspend fun getCategoriesFromCache(): List<Category>? {
        return withContext(Dispatchers.IO) {
            try {
                categoryDao.getAll()
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
                    recipesDao.insertAll(recipes.filterNotNull())
                }
            }
            recipes?.filterNotNull() ?: emptyList()
        }
    }

    private suspend fun getRecipesByCategoryIdFromBackEnd(categoryId: Int = 0): List<Recipe?>? {
        return withContext(Dispatchers.IO) {
            try {
                val call: Call<List<Recipe?>?>? = recipeApiService.getRecipes(categoryId)
                call?.execute()?.body()
            } catch (e: Exception) {
                null
            }
        }
    }

    private suspend fun getRecipesByCategoryIdFromCache(categoryId: Int = 0): List<Recipe?>? {
        return withContext(Dispatchers.IO) {
            try {
                recipesDao.getByCategoryId(categoryId)
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
                    recipesDao.insertRecipe(recipe)
                }
            }
            recipe
        }
    }

    private suspend fun getRecipeByIdFromBackEnd(recipesId: Int): Recipe? {
        return withContext(Dispatchers.IO) {
            try {
                val call: Call<Recipe?>? = recipeApiService.getRecipe(recipesId)
                call?.execute()?.body()
            } catch (e: Exception) {
                null
            }
        }
    }

    private suspend fun getRecipeByIdFromCache(recipesId: Int): Recipe? {
        return withContext(Dispatchers.IO) {
            try {
                recipesDao.getById(recipesId)
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun setFavorite(recipesId: Int?) {
        return withContext(Dispatchers.IO) {
            try {
                recipesId?.let { recipesDao.setFavorites(it) }
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getFavorites(): List<Recipe?>? {
        return withContext(Dispatchers.IO) {
            try {
                recipesDao.getFavorites()
            } catch (e: Exception) {
                null
            }
        }
    }
}