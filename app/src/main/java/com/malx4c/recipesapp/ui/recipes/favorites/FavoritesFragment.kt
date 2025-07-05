package com.malx4c.recipesapp.ui.recipes.favorites

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.malx4c.recipesapp.ARG_RECIPE
import com.malx4c.recipesapp.PREFS_KEY_FAVORITES
import com.malx4c.recipesapp.PREFS_NAME
import com.malx4c.recipesapp.R
import com.malx4c.recipesapp.ui.recipes.recipe.RecipeFragment
import com.malx4c.recipesapp.ui.recipes.recipeList.RecipesListAdapter
import com.malx4c.recipesapp.data.STUB
import com.malx4c.recipesapp.databinding.FragmentFavoritesBinding
import com.malx4c.recipesapp.model.Recipe

class FavoritesFragment : Fragment() {
    private var prefs: SharedPreferences? = null
    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("FragmentFavoritesBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        prefs = activity?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        _binding = FragmentFavoritesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
    }

    private fun initRecycler() {
        val recipesIds: Set<Int> = getFavorites().map{it.toInt()}.toSet()

        if (recipesIds.isEmpty()) {
            binding.tvTitleEmptyFavorites.visibility = View.VISIBLE
            return
        }

        val recipesAdapter = RecipesListAdapter(STUB.getRecipesByIds(recipesIds))
        binding.rvRecipes.adapter = recipesAdapter

        recipesAdapter.setOnItemClickListener(object : RecipesListAdapter.OnItemClickListener {
            override fun onItemClick(recipesId: Int) {
                openRecipeByRecipeId(recipesId)
            }
        })
    }

    private fun getFavorites(): MutableSet<String> {
        val favoritesList: Set<String>? = prefs?.getStringSet(PREFS_KEY_FAVORITES, null)

        return HashSet(favoritesList ?: emptySet())
    }

    private fun openRecipeByRecipeId(recipesId: Int) {

        val recipe: Recipe? = STUB.getRecipeById(recipesId)
        val bundle = Bundle().apply {
            putParcelable(ARG_RECIPE, recipe)
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