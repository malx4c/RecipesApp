package com.malx4c.recipesapp.ui.recipes.recipe

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.malx4c.recipesapp.ARG_RECIPE_ID
import com.malx4c.recipesapp.ui.IngredientsAdapter
import com.malx4c.recipesapp.ui.MethodAdapter
import com.malx4c.recipesapp.R
import com.malx4c.recipesapp.databinding.FragmentRecipeBinding
import com.malx4c.recipesapp.model.Recipe

class RecipeFragment : Fragment() {
    private val recipeViewModel: RecipeViewModel by viewModels()
    private var recipe: Recipe? = null
    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("FragmentRecipeBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRecipeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        initRecycler()
    }

    private fun initUI() {
        val recipesId: Int? = arguments?.getInt(ARG_RECIPE_ID)
        recipesId?.let { recipeViewModel.loadRecipe(it) }

        val recipeObserver = Observer<RecipeViewModel.RecipeUiState> {
            binding.tvRecipeTitle.text = it.recipe?.title
            val imageFavoritesId: Int =
                if (it.isFavorites) R.drawable.ic_heart else R.drawable.ic_heart_empty
            binding.btnSetFavorites.setImageResource(imageFavoritesId)
            binding.ivRecipe.setImageDrawable(it.recipeImage)
            recipe = it.recipe
            initRecycler()
        }

        recipeViewModel.recipeState.observe(viewLifecycleOwner, recipeObserver)
        binding.btnSetFavorites.setOnClickListener { recipeViewModel.onFavoritesClicked() }
        updateNumberServings()
    }

    private fun initRecycler() {
        val ingredientsAdapter = recipe?.let { IngredientsAdapter(it) }
        binding.rvIngredients.adapter = ingredientsAdapter
        binding.rvIngredients.addItemDecoration(getDivider(binding.rvMethod.context))

        val methodAdapter = recipe?.let { MethodAdapter(it) }
        binding.rvMethod.adapter = methodAdapter
        binding.rvMethod.addItemDecoration(getDivider(binding.rvMethod.context))

        binding.sbNumberServings.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                ingredientsAdapter?.updateIngredients(progress)
                updateNumberServings(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

    private fun updateNumberServings(numberServings: Int = 1) {
        binding.tvNumberServings.text = numberServings.toString()
    }

    private fun getDivider(context: Context) =
        MaterialDividerItemDecoration(context, LinearLayoutManager.VERTICAL).apply {
            isLastItemDecorated = false
            dividerColor = ContextCompat.getColor(context, R.color.divider_rv_item_color)
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}