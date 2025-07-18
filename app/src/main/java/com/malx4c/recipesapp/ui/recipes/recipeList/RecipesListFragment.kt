package com.malx4c.recipesapp.ui.recipes.recipeList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import com.malx4c.recipesapp.ARG_RECIPE_ID
import com.malx4c.recipesapp.R
import com.malx4c.recipesapp.ui.recipes.recipe.RecipeFragment
import com.malx4c.recipesapp.databinding.FragmentListRecipesBinding

class RecipesListFragment : Fragment() {

    private val recipesListViewModel: RecipesListViewModel by viewModels()
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

        recipesListViewModel.loadRecipesByCategoryId(arguments)
        val category = recipesListViewModel.recipesListState.value

        binding.tvRecipesTitle.text = category?.categoryName
        binding.ivRecipes.apply {
            setImageDrawable(category?.categoryImage)
            contentDescription = category?.categoryName
        }

        initRecipes()
    }

    private fun initRecipes() {

        val recipesAdapter = recipesListViewModel.recipesListState.value?.recipes?.let {
            RecipesListAdapter(it)
        }
        binding.rvRecipes.adapter = recipesAdapter

        recipesAdapter?.setOnItemClickListener(object : RecipesListAdapter.OnItemClickListener {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}