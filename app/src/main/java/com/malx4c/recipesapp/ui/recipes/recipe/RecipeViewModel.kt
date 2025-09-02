package com.malx4c.recipesapp.ui.recipes.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malx4c.recipesapp.API_IMAGE_SOURCE
import com.malx4c.recipesapp.API_URL
import com.malx4c.recipesapp.ERROR_MESSAGE_FETCH_DATA
import com.malx4c.recipesapp.data.RecipesRepository
import com.malx4c.recipesapp.model.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(private val recipeRepository: RecipesRepository) :
    ViewModel() {

    data class RecipeUiState(
        val portionsCount: Int = 1,
        var recipe: Recipe? = null,
        val recipeImageUrl: String? = null
    )

    private var _recipeState = MutableLiveData(RecipeUiState())
    val recipeState: LiveData<RecipeUiState>
        get() = _recipeState

    private val _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message

    fun loadRecipe(recipesId: Int) {
        viewModelScope.launch {
            val recipe = recipeRepository.getRecipeById(recipesId)
            if (recipe !== null) {
                _recipeState.value = recipeState.value?.copy(
                    recipe = recipe,
                    recipeImageUrl = "$API_URL$API_IMAGE_SOURCE${recipe.imageUrl}"
                )
            } else {
                _message.postValue(ERROR_MESSAGE_FETCH_DATA)
            }
        }
    }

    fun onFavoritesClicked() {
        val current: Boolean = recipeState.value?.recipe?.isFavorite ?: false
        val recipeId = recipeState.value?.recipe?.id

        _recipeState.value = recipeState.value?.copy(
            recipe = recipeState.value?.recipe?.copy(isFavorite = !current)
        )

        viewModelScope.launch {
            recipeRepository.setFavorite(recipeId)
        }
    }

    fun updateNumberPortions(portionsCount: Int = 1) {
        _recipeState.value = recipeState.value?.copy(
            portionsCount = portionsCount
        )
    }
}