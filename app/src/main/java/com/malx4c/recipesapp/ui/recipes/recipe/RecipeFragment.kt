package com.malx4c.recipesapp.ui.recipes.recipe

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.malx4c.recipesapp.ui.IngredientsAdapter
import com.malx4c.recipesapp.ui.MethodAdapter
import com.malx4c.recipesapp.R
import com.malx4c.recipesapp.RecipesApplication
import com.malx4c.recipesapp.databinding.FragmentRecipeBinding

class PortionSeekBarListener(val onChangeIngredients: (Int) -> Unit) :
    SeekBar.OnSeekBarChangeListener {
    override fun onProgressChanged(
        seekBar: SeekBar?,
        progress: Int,
        fromUser: Boolean
    ) {
        onChangeIngredients(progress)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}
    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
}

class RecipeFragment : Fragment() {
    private val args: RecipeFragmentArgs by navArgs()
    private lateinit var recipeViewModel: RecipeViewModel
    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("FragmentRecipeBinding is null")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContainer = (requireActivity().application as RecipesApplication).appContainer
        recipeViewModel = appContainer.recipeViewModelFactory.create()
    }


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
    }

    private fun initUI() {
        val recipesId: Int = args.recipeId
        recipesId.let { recipeViewModel.loadRecipe(it) }

        val ingredientsAdapter = IngredientsAdapter()
        binding.rvIngredients.adapter = ingredientsAdapter
        binding.rvIngredients.addItemDecoration(getDivider(binding.rvMethod.context))

        val methodAdapter = MethodAdapter()
        binding.rvMethod.adapter = methodAdapter
        binding.rvMethod.addItemDecoration(getDivider(binding.rvMethod.context))

        val recipeObserver = Observer<RecipeViewModel.RecipeUiState> {
            binding.tvRecipeTitle.text = it.recipe?.title
            val imageFavoritesId: Int =
                if (it.recipe?.isFavorite == true) R.drawable.ic_heart else R.drawable.ic_heart_empty

            binding.btnSetFavorites.setImageResource(imageFavoritesId)
            Glide.with(this)
                .load(it.recipeImageUrl)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .into(binding.ivRecipe)

            binding.tvNumberServings.text = it.portionsCount.toString()
            binding.sbNumberServings.setOnSeekBarChangeListener(
                PortionSeekBarListener { progress ->
                    ingredientsAdapter.updateIngredients(progress)
                    recipeViewModel.updateNumberPortions(progress)
                }
            )
            methodAdapter.update(it.recipe?.method ?: emptyList())
            ingredientsAdapter.update(it.recipe?.ingredients ?: emptyList())
        }

        recipeViewModel.recipeState.observe(viewLifecycleOwner, recipeObserver)
        binding.btnSetFavorites.setOnClickListener { recipeViewModel.onFavoritesClicked() }

        recipeViewModel.message.observe(viewLifecycleOwner, Observer { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        })
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