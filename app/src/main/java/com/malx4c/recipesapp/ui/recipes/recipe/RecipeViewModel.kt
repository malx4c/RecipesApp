package com.malx4c.recipesapp.ui.recipes.recipe

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.malx4c.recipesapp.PREFS_KEY_FAVORITES
import com.malx4c.recipesapp.PREFS_NAME
import com.malx4c.recipesapp.data.STUB
import com.malx4c.recipesapp.model.Recipe

class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    private var prefs = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    data class RecipeUiState(
        val portionsCount: Int = 1,
        var isFavorites: Boolean = false,
        val recipe: Recipe? = null,
    )

    private var _recipeState = MutableLiveData(RecipeUiState())
    val recipeState: LiveData<RecipeUiState>
        get() = _recipeState

    /* TODO load from network*/
    fun loadRecipe(recipesId: Int) {
        _recipeState.value = _recipeState.value?.copy(
            isFavorites = getFavorites().contains(recipesId.toString()),
            recipe = STUB.getRecipeById(recipesId)
        )
    }

    fun onFavoritesClicked() {
        val current: Boolean = _recipeState.value?.isFavorites ?: false
        _recipeState.value = _recipeState.value?.copy(
            isFavorites = !current
        )

        val recipeId = _recipeState.value?.recipe?.id
        val favorites = getFavorites()

        if (favorites.contains(recipeId.toString()))
            favorites.remove(recipeId.toString()) else favorites.add(recipeId.toString())

        saveFavorites(favorites)
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