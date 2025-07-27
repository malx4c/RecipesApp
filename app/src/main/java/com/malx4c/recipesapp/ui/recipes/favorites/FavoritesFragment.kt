package com.malx4c.recipesapp.ui.recipes.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.malx4c.recipesapp.ARG_RECIPE_ID
import com.malx4c.recipesapp.R
import com.malx4c.recipesapp.ui.recipes.recipeList.RecipesListAdapter
import com.malx4c.recipesapp.databinding.FragmentFavoritesBinding

class FavoritesFragment : Fragment() {

    private val favoritesViewModel: FavoritesViewModel by viewModels()
    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("FragmentFavoritesBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFavoritesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
    }

    private fun initRecycler() {
        val recipesAdapter = RecipesListAdapter(emptyList())
        binding.rvRecipes.adapter = recipesAdapter

        val favoritesObserver = Observer<FavoritesViewModel.FavoritesUiState> {
            if (it.recipes?.isEmpty() == true) {
                binding.tvTitleEmptyFavorites.visibility = View.VISIBLE
            } else {
                it.recipes?.let { recipesAdapter.update(it) }
                recipesAdapter.setOnItemClickListener(object :
                    RecipesListAdapter.OnItemClickListener {
                    override fun onItemClick(recipesId: Int) {
                        openRecipeByRecipeId(recipesId)
                    }
                })
            }
        }

        favoritesViewModel.favoritesState.observe(
            viewLifecycleOwner,
            favoritesObserver
        )
    }

    private fun openRecipeByRecipeId(recipesId: Int) {
        val bundle = Bundle().apply {
            putInt(ARG_RECIPE_ID, recipesId)
        }

        findNavController().navigate(R.id.recipeFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}