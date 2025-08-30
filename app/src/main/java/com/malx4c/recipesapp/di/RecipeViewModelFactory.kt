package com.malx4c.recipesapp.di

import com.malx4c.recipesapp.data.RecipesRepository
import com.malx4c.recipesapp.ui.recipes.recipe.RecipeViewModel

class RecipeViewModelFactory(private val recipesRepository: RecipesRepository) :
    Factory<RecipeViewModel> {

    override fun create(): RecipeViewModel {
        return RecipeViewModel(recipesRepository)
    }
}