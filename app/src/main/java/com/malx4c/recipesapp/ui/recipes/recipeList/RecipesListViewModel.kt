package com.malx4c.recipesapp.ui.recipes.recipeList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.malx4c.recipesapp.API_IMAGE_SOURCE
import com.malx4c.recipesapp.API_URL
import com.malx4c.recipesapp.ERROR_MESSAGE_FETCH_DATA
import com.malx4c.recipesapp.data.RecipesRepository
import com.malx4c.recipesapp.model.Category
import com.malx4c.recipesapp.model.Recipe
import kotlinx.coroutines.launch

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
            categoryImageUrl = "$API_URL$API_IMAGE_SOURCE${category.imageUrl}",
        )
        getRecipesByCategoryId(category.id)
    }

    private fun getRecipesByCategoryId(categoryId: Int?) {
        viewModelScope.launch {
            val _recipes = categoryId?.let { recipeRepository.getRecipesByCategoryId(it) }
            if (_recipes !== null) {
                _recipesListState.value = recipesListState.value?.copy(
                    recipes = _recipes
                )
            } else {
                _message.postValue(ERROR_MESSAGE_FETCH_DATA)
            }
        }
    }
}