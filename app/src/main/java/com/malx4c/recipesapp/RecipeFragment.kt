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
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.malx4c.recipesapp.databinding.FragmentRecipeBinding
import com.malx4c.recipesapp.entities.Recipe

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
        binding.btnSetFavorites.setImageResource(R.drawable.ic_heart_empty)
        binding.btnSetFavorites.setOnClickListener { binding.btnSetFavorites.setImageResource(R.drawable.ic_heart) }
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