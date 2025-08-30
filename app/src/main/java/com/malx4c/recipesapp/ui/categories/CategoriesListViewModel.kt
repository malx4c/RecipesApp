package com.malx4c.recipesapp.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malx4c.recipesapp.ERROR_MESSAGE_FETCH_DATA
import com.malx4c.recipesapp.data.RecipesRepository
import com.malx4c.recipesapp.model.Category
import kotlinx.coroutines.launch

class CategoriesListViewModel(
    private val recipeRepository: RecipesRepository
) : ViewModel() {

    data class CategoriesListUiState(
        var categories: List<Category?>? = null
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
        viewModelScope.launch {
            val categoriesNew = recipeRepository.getCategories()
            if (categoriesNew != null) {
                _categoriesListViewState.postValue(
                    categoriesListViewState.value?.copy(
                        categories = categoriesNew
                    )
                )
            } else {
                _message.postValue(ERROR_MESSAGE_FETCH_DATA)
            }
        }
    }

    fun getCategoryById(categoryId: Int): Category? {
        return categoriesListViewState.value?.categories?.find { it?.id == categoryId }
    }
}