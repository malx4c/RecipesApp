package com.malx4c.recipesapp.ui.recipes.recipeList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.malx4c.recipesapp.data.STUB
import com.malx4c.recipesapp.model.Category
import com.malx4c.recipesapp.model.Recipe

class RecipesListViewModel(application: Application) : AndroidViewModel(application) {

    data class RecipesListUiState(
        val categoryId: Int? = null,
        val categoryName: String? = null,
        val categoryImageUrl: String? = null,
        var recipes: List<Recipe>? = emptyList()
    )

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

    private fun getRecipesByCategoryId(categoryId: Int?): List<Recipe>? {
        return categoryId?.let { STUB.getRecipesByCategoryId(it) }
    }
}