package com.malx4c.recipesapp.ui.recipes.recipeList

import android.app.Application
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.malx4c.recipesapp.ARG_CATEGORY_ID
import com.malx4c.recipesapp.ARG_CATEGORY_IMAGE_URL
import com.malx4c.recipesapp.ARG_CATEGORY_NAME
import com.malx4c.recipesapp.data.STUB
import com.malx4c.recipesapp.model.Recipe


class RecipesListViewModel(application: Application) : AndroidViewModel(application) {

    data class RecipesListUiState(
        val categoryId: Int? = null,
        val categoryName: String? = null,
        val categoryImage: Drawable? = null,
        var recipes: List<Recipe>? = emptyList()
    )

    private var _recipesListState = MutableLiveData(RecipesListUiState())
    val recipesListState: LiveData<RecipesListUiState>
        get() = _recipesListState

    fun loadRecipesByCategoryId(arguments: Bundle?) {
        arguments?.let {
            _recipesListState.value = RecipesListUiState(
                categoryId = it.getInt(ARG_CATEGORY_ID),
                categoryName = it.getString(ARG_CATEGORY_NAME),
                categoryImage = getCategoryImage(it.getString(ARG_CATEGORY_IMAGE_URL)),
                recipes = getRecipesByCategoryId(it.getInt(ARG_CATEGORY_ID))
            )
        }
    }

    private fun getCategoryImage(categoryImageUrl: String?): Drawable? {
        val context = getApplication<Application>().applicationContext
        val drawable = try {
            Drawable.createFromStream(categoryImageUrl?.let { context.assets.open(it) }, null)
        } catch (e: Exception) {
            Log.e("!!! file open error", categoryImageUrl, e)
            null
        }

        return drawable
    }

    private fun getRecipesByCategoryId(categoryId: Int?): List<Recipe>? {
        return categoryId?.let { STUB.getRecipesByCategoryId(it) }
    }
}