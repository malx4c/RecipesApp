package com.malx4c.recipesapp.ui.recipes.recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.malx4c.recipesapp.model.Recipe

class RecipeViewModel : ViewModel() {

    data class RecipeUiState(
        val numberServings: Int = 1,
        var isFavorites: Boolean = false,
        val recipe: Recipe? = null,
    )

    private val _recipeState = MutableLiveData<RecipeUiState>()
    val recipeState: LiveData<RecipeUiState>
        get() = _recipeState

    init {
        Log.i("!!!!", "init RecipeViewModel")
        val newState = RecipeUiState(
            isFavorites = true,
        )
        _recipeState.value = newState
    }
}