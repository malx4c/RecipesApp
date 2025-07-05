package com.malx4c.recipesapp.ui.recipes.recipe

import androidx.lifecycle.ViewModel
import com.malx4c.recipesapp.model.Ingredient

class RecipeViewMode : ViewModel() {

    data class RecipeUiState(
        val numberServings: Int = 1,
        val isFavorites: Boolean = false,
        val id: Int? = null,
        val title: String? = null,
        val ingredients: List<Ingredient> = emptyList(),
        val method: List<String> = emptyList(),
        val imageUrl: String? = null
    )
}