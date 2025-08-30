package com.malx4c.recipesapp.ui.recipes.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.malx4c.recipesapp.RecipesApplication
import com.malx4c.recipesapp.ui.recipes.recipeList.RecipesListAdapter
import com.malx4c.recipesapp.databinding.FragmentFavoritesBinding

class FavoritesFragment : Fragment() {

    private lateinit var favoritesViewModel: FavoritesViewModel

    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("FragmentFavoritesBinding is null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContainer = (requireActivity().application as RecipesApplication).appContainer
        favoritesViewModel = appContainer.favoritesViewModelFactory.create()
    }


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
                binding.tvTitleEmptyFavorites.visibility = View.INVISIBLE
                it.recipes?.let { recipesAdapter.update(it) }
                recipesAdapter.setOnItemClickListener(object :
                    RecipesListAdapter.OnItemClickListener {
                    override fun onItemClick(recipesId: Int) {
                        val action =
                            FavoritesFragmentDirections.actionFavoritesFragmentToRecipeFragment(
                                recipesId
                            )
                        view?.findNavController()?.navigate(action)
                    }
                })
            }
        }

        favoritesViewModel.favoritesState.observe(
            viewLifecycleOwner,
            favoritesObserver
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}