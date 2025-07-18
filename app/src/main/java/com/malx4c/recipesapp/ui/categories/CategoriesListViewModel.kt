package com.malx4c.recipesapp.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.malx4c.recipesapp.data.STUB
import com.malx4c.recipesapp.model.Category

class CategoriesListViewModel : ViewModel() {

    data class CategoriesListUiState(
        var categories: List<Category> = emptyList()
    )

    private var _categoriesListViewState = MutableLiveData(CategoriesListUiState())
    val categoriesListViewState: LiveData<CategoriesListUiState>
        get() = _categoriesListViewState

    init {
        loadCategories()
    }

    private fun loadCategories() {
        _categoriesListViewState.value?.categories = STUB.getCategories()
    }

    fun getCategoryById(categoryId: Int): Category? {
        return categoriesListViewState.value?.categories?.find { it.id == categoryId }
    }
}