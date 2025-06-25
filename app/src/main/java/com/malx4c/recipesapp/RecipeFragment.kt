package com.malx4c.recipesapp

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.malx4c.recipesapp.databinding.FragmentRecipeBinding
import com.malx4c.recipesapp.entities.Recipe
import androidx.core.graphics.toColorInt

class RecipeFragment : Fragment() {

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

        initUI(view)
        initRecycler()
    }

    private fun initUI(view: View) {
        arguments?.let {
            recipe = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable(ARG_RECIPE, Recipe::class.java)
            } else {
                it.getParcelable(ARG_RECIPE)
            }
        }

        val drawable = try {
            Drawable.createFromStream(recipe?.imageUrl?.let { view.context.assets.open(it) }, null)
        } catch (e: Exception) {
            Log.e("!!! image open error", recipe?.imageUrl, e)
            null
        }

        binding.ivRecipe.setImageDrawable(drawable)
        binding.tvRecipeTitle.text = recipe?.title
    }

    private fun initRecycler() {
        val ingredientsAdapter = recipe?.let { IngredientsAdapter(it) }
        binding.rvIngredients.adapter = ingredientsAdapter
        binding.rvIngredients.addItemDecoration(getDivider(binding.rvMethod.context))

        val methodAdapter = recipe?.let { MethodAdapter(it) }
        binding.rvMethod.adapter = methodAdapter
        binding.rvMethod.addItemDecoration(getDivider(binding.rvMethod.context))
    }

    private fun getDivider(context: Context) =
        MaterialDividerItemDecoration(context, LinearLayoutManager.VERTICAL).apply {
            isLastItemDecorated = false
            dividerColor = DIVIDER_COLOR.toColorInt()
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}