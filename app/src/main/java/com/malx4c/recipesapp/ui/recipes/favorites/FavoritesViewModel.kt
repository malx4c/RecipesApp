package com.malx4c.recipesapp.ui.recipes.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.malx4c.recipesapp.data.RecipesRepository
import com.malx4c.recipesapp.model.Recipe
import kotlinx.coroutines.launch

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {
    private val recipeRepository = RecipesRepository(application.applicationContext)

    data class FavoritesUiState(
        var recipes: List<Recipe?>? = emptyList(),
    )

    private var _favoritesState = MutableLiveData(FavoritesUiState())
    val favoritesState: LiveData<FavoritesUiState>
        get() = _favoritesState

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            val _recipes = recipeRepository.getFavorites() ?: emptyList()
            _favoritesState.value = favoritesState.value?.copy(
                recipes = _recipes
            )
        }
    }
}
