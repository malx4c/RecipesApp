package com.malx4c.recipesapp.ui.recipes.recipeList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.malx4c.recipesapp.R
import com.malx4c.recipesapp.databinding.FragmentListRecipesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipesListFragment : Fragment() {
    private val args: RecipesListFragmentArgs by navArgs()
    private var _binding: FragmentListRecipesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("FragmentListRecipesBinding is null")

    private val recipesListViewModel: RecipesListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentListRecipesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recipesListViewModel.loadRecipesByCategory(args.category)
        initRecipesTitle()
        initRecipes()
    }

    private fun initRecipesTitle() {
        val categoryObserver = Observer<RecipesListViewModel.RecipesListUiState> {
            binding.tvRecipesTitle.text = it.categoryName
            Glide.with(this)
                .load(it.categoryImageUrl)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .into(binding.ivRecipes)
            binding.ivRecipes.contentDescription = it.categoryName
        }

        recipesListViewModel.recipesListState.observe(
            viewLifecycleOwner,
            categoryObserver
        )
    }

    private fun initRecipes() {

        val recipesListAdapter = RecipesListAdapter(emptyList())
        binding.rvRecipes.adapter = recipesListAdapter

        val recipesObserver = Observer<RecipesListViewModel.RecipesListUiState> {
            it.recipes?.let { recipesListAdapter.update(it) }

            recipesListAdapter.setOnItemClickListener(object :
                RecipesListAdapter.OnItemClickListener {
                override fun onItemClick(recipesId: Int) {
                    val action =
                        RecipesListFragmentDirections.actionRecipesListFragmentToRecipeFragment(
                            recipesId
                        )
                    view?.findNavController()?.navigate(action)
                }
            })
        }

        recipesListViewModel.recipesListState.observe(
            viewLifecycleOwner,
            recipesObserver
        )

        recipesListViewModel.message.observe(viewLifecycleOwner, Observer { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}