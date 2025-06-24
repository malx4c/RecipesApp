package com.malx4c.recipesapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malx4c.recipesapp.databinding.ItemIngredientsBinding
import com.malx4c.recipesapp.entities.Ingredient
import com.malx4c.recipesapp.entities.Recipe


class IngredientsAdapter(private val recipe: Recipe) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    private val dataSet: List<Ingredient> = recipe.ingredients

    inner class ViewHolder(private val binding: ItemIngredientsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(ingredients: Ingredient) {

            binding.apply {
                tvIngredientDescription.text = ingredients.description
                tvIngredientQuantity.text = ingredients.quantity + ' ' + ingredients.unitOfMeasure
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {

        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = ItemIngredientsBinding.inflate(inflater, viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.bind(dataSet[position])
    }

    override fun getItemCount() = dataSet.size
}