package com.malx4c.recipesapp.ui.recipes.favorites

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.malx4c.recipesapp.PREFS_KEY_FAVORITES
import com.malx4c.recipesapp.PREFS_NAME
import com.malx4c.recipesapp.data.STUB
import com.malx4c.recipesapp.model.Recipe

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {
    private var prefs = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    data class FavoritesUiState(
        var recipes: List<Recipe>? = emptyList(),
    )

    private var _favoritesState = MutableLiveData(FavoritesUiState())
    val favoritesState: LiveData<FavoritesUiState>
        get() = _favoritesState

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        val favoritesList: Set<String>? = prefs?.getStringSet(PREFS_KEY_FAVORITES, null)
        val recipesIds: Set<Int>? = favoritesList?.map { it.toInt() }?.toSet()

        _favoritesState.value?.recipes = recipesIds?.let { STUB.getRecipesByIds(it) }
    }
}