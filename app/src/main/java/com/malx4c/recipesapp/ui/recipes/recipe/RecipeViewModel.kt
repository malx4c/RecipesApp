package com.malx4c.recipesapp.ui.recipes.recipe

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.malx4c.recipesapp.API_IMAGE_SOURCE
import com.malx4c.recipesapp.API_URL
import com.malx4c.recipesapp.ERROR_MESSAGE_FETCH_DATA
import com.malx4c.recipesapp.PREFS_KEY_FAVORITES
import com.malx4c.recipesapp.PREFS_NAME
import com.malx4c.recipesapp.data.RecipesRepository
import com.malx4c.recipesapp.model.Recipe

class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    private val recipeRepository = RecipesRepository()
    private var prefs = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    data class RecipeUiState(
        val portionsCount: Int = 1,
        var isFavorites: Boolean = false,
        val recipe: Recipe? = null,
        val recipeImageUrl: String? = null
    )

    private var _recipeState = MutableLiveData(RecipeUiState())
    val recipeState: LiveData<RecipeUiState>
        get() = _recipeState

    private val _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message


    /* TODO load from network*/
    fun loadRecipe(recipesId: Int) {
        val recipe = recipeRepository.getRecipeById(recipesId)
        if (recipe !== null) {
            _recipeState.value = recipeState.value?.copy(
                isFavorites = getFavorites().contains(recipesId.toString()),
                recipe = recipe,
                recipeImageUrl = "$API_URL$API_IMAGE_SOURCE${recipe.imageUrl}"
            )
        } else {
            _message.postValue(ERROR_MESSAGE_FETCH_DATA)
        }
    }

    fun onFavoritesClicked() {
        val current: Boolean = recipeState.value?.isFavorites ?: false
        _recipeState.value = recipeState.value?.copy(
            isFavorites = !current
        )

        val recipeId = recipeState.value?.recipe?.id
        val favorites = getFavorites()

        if (favorites.contains(recipeId.toString()))
            favorites.remove(recipeId.toString()) else favorites.add(recipeId.toString())

        saveFavorites(favorites)
    }

    fun updateNumberPortions(portionsCount: Int = 1) {
        _recipeState.value = recipeState.value?.copy(
            portionsCount = portionsCount
        )
    }

    private fun saveFavorites(favorites: MutableSet<String>) {
        val prefsEditor = prefs?.edit()
        prefsEditor?.let {
            it.putStringSet(PREFS_KEY_FAVORITES, favorites)
            it.apply()
        }
    }

    private fun getFavorites(): MutableSet<String> {
        val favoritesList: Set<String>? = prefs?.getStringSet(PREFS_KEY_FAVORITES, null)

        return HashSet(favoritesList ?: emptySet())
    }
}