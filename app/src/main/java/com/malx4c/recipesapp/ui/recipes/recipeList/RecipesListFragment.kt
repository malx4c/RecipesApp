package com.malx4c.recipesapp.ui.recipes.recipeList

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.malx4c.recipesapp.ARG_CATEGORY_ID
import com.malx4c.recipesapp.ARG_CATEGORY_IMAGE_URL
import com.malx4c.recipesapp.ARG_CATEGORY_NAME
import com.malx4c.recipesapp.ARG_RECIPE_ID
import com.malx4c.recipesapp.R
import com.malx4c.recipesapp.ui.recipes.recipe.RecipeFragment
import com.malx4c.recipesapp.data.STUB
import com.malx4c.recipesapp.databinding.FragmentListRecipesBinding

class RecipesListFragment : Fragment() {

    private var categoryId: Int? = null
    private var categoryName: String? = null
    private var categoryImageUrl: String? = null

    private var _binding: FragmentListRecipesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("FragmentListRecipesBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentListRecipesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            categoryId = it.getInt(ARG_CATEGORY_ID)
            categoryName = it.getString(ARG_CATEGORY_NAME)
            categoryImageUrl = it.getString(ARG_CATEGORY_IMAGE_URL)
        }

        val drawable = try {
            Drawable.createFromStream(categoryImageUrl?.let { view.context.assets.open(it) }, null)
        } catch (e: Exception) {
            Log.e("!!! file open error", categoryImageUrl, e)
            null
        }

        binding.tvRecipesTitle.text = categoryName
        binding.ivRecipes.apply {
            setImageDrawable(drawable)
            contentDescription = categoryName
        }

        initRecipes()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecipes() {
        val recipesAdapter = RecipesListAdapter(STUB.getRecipesByCategoryId())
        binding.rvRecipes.adapter = recipesAdapter


        recipesAdapter.setOnItemClickListener(object : RecipesListAdapter.OnItemClickListener {
            override fun onItemClick(recipesId: Int) {
                openRecipeByRecipeId(recipesId)
            }
        })
    }

    private fun openRecipeByRecipeId(recipesId: Int) {
        val bundle = Bundle().apply {
            putInt(ARG_RECIPE_ID, recipesId)
        }

        parentFragmentManager.commit {
            replace<RecipeFragment>(R.id.mainContainer, args = bundle)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }
}