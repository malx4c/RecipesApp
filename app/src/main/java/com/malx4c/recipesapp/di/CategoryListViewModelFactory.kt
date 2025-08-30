package com.malx4c.recipesapp.di

import com.malx4c.recipesapp.data.RecipesRepository
import com.malx4c.recipesapp.ui.categories.CategoriesListViewModel

class CategoryListViewModelFactory(private val recipesRepository: RecipesRepository) :
    Factory<CategoriesListViewModel> {

    override fun create(): CategoriesListViewModel {
        return CategoriesListViewModel(recipesRepository)
    }
}