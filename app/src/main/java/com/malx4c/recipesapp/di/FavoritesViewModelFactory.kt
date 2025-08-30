package com.malx4c.recipesapp.di

import com.malx4c.recipesapp.data.RecipesRepository
import com.malx4c.recipesapp.ui.recipes.favorites.FavoritesViewModel

class FavoritesViewModelFactory(private val recipesRepository: RecipesRepository) :
    Factory<FavoritesViewModel> {

    override fun create(): FavoritesViewModel {
        return FavoritesViewModel(recipesRepository)
    }
}