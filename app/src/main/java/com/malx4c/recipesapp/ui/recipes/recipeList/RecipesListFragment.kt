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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
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
            setImageDrawable(getCategoryImage(category?.categoryImageUrl))
            contentDescription = category?.categoryName
        }

        initRecipes()
    }

    private fun initRecipes() {

        val recipesListAdapter = RecipesListAdapter(emptyList())
        binding.rvRecipes.adapter = recipesListAdapter

        val recipesObserver = Observer<RecipesListViewModel.RecipesListUiState> {
            it.recipes?.let { recipesListAdapter.update(it) }

            recipesListAdapter.setOnItemClickListener(object :
                RecipesListAdapter.OnItemClickListener {
                override fun onItemClick(recipesId: Int) {
                    openRecipeByRecipeId(recipesId)
                }
            })
        }

        recipesListViewModel.recipesListState.observe(
            viewLifecycleOwner,
            recipesObserver
        )
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

    private fun getCategoryImage(categoryImageUrl: String?): Drawable? {
        val drawable = try {
            Drawable.createFromStream(
                categoryImageUrl?.let { view?.context?.assets?.open(it) },
                null
            )
        } catch (e: Exception) {
            Log.e("!!! file open error", categoryImageUrl, e)
            null
        }

        return drawable
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}