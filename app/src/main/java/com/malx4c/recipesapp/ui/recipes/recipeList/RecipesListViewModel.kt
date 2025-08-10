package com.malx4c.recipesapp.ui.recipes.recipeList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.malx4c.recipesapp.ERROR_MESSAGE_FETCH_DATA
import com.malx4c.recipesapp.data.RecipesRepository
import com.malx4c.recipesapp.model.Category
import com.malx4c.recipesapp.model.Recipe

class RecipesListViewModel(application: Application) : AndroidViewModel(application) {
    private val recipeRepository = RecipesRepository()

    data class RecipesListUiState(
        val categoryId: Int? = null,
        val categoryName: String? = null,
        val categoryImageUrl: String? = null,
        var recipes: List<Recipe?>? = null
    )

    private val _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message

    private var _recipesListState = MutableLiveData(RecipesListUiState())
    val recipesListState: LiveData<RecipesListUiState>
        get() = _recipesListState

    fun loadRecipesByCategory(category: Category) {
        _recipesListState.value = RecipesListUiState(
            categoryId = category.id,
            categoryName = category.title,
            categoryImageUrl = category.imageUrl,
            recipes = getRecipesByCategoryId(category.id)
        )
    }

    private fun getRecipesByCategoryId(categoryId: Int?): List<Recipe?>? {
        return categoryId?.let { recipeRepository.getRecipesByCategoryId(it) } ?: run {
            _message.postValue(ERROR_MESSAGE_FETCH_DATA)
            null
        }
    }
}