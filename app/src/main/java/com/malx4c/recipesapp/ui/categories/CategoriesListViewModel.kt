package com.malx4c.recipesapp.ui.categories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.malx4c.recipesapp.data.RecipesRepository
import com.malx4c.recipesapp.model.Category

class CategoriesListViewModel : ViewModel() {
    private val recipeRepository = RecipesRepository()

    data class CategoriesListUiState(
        var categories: List<Category> = emptyList()
    )

    private val _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message

    private var _categoriesListViewState = MutableLiveData(CategoriesListUiState())
    val categoriesListViewState: LiveData<CategoriesListUiState>
        get() = _categoriesListViewState

    init {
        loadCategories()
    }

    private fun loadCategories() {
        try {
            val categoriesNew = recipeRepository.getCategories() as List<Category>
            _categoriesListViewState.value = categoriesListViewState.value?.copy(
                categories = categoriesNew
            )
        } catch (e: Exception) {
            Log.e("!!! loadCategories error: ", e.stackTrace.toString())
            _message.postValue("Ошибка получения данных")
        }
    }

    fun getCategoryById(categoryId: Int): Category? {
        return categoriesListViewState.value?.categories?.find { it.id == categoryId }
    }
}