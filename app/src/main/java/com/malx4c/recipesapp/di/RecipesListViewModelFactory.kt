package com.malx4c.recipesapp.di

import com.malx4c.recipesapp.data.RecipesRepository
import com.malx4c.recipesapp.ui.recipes.recipeList.RecipesListViewModel

class RecipesListViewModelFactory(private val recipesRepository: RecipesRepository) :
    Factory<RecipesListViewModel> {

    override fun create(): RecipesListViewModel {
        return RecipesListViewModel(recipesRepository)
    }
}