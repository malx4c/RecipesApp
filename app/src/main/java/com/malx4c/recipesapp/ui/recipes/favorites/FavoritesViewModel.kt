package com.malx4c.recipesapp.ui.recipes.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malx4c.recipesapp.data.RecipesRepository
import com.malx4c.recipesapp.model.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(private val recipeRepository: RecipesRepository) :
    ViewModel() {

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
