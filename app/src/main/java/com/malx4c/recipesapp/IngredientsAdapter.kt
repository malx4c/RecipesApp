package com.malx4c.recipesapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malx4c.recipesapp.databinding.ItemIngredientsBinding
import com.malx4c.recipesapp.entities.Ingredient
import com.malx4c.recipesapp.entities.Recipe
import java.math.BigDecimal
import java.math.RoundingMode

class IngredientsAdapter(private val recipe: Recipe) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    private val dataSet: List<Ingredient> = recipe.ingredients
    private var quantity: Int = 1

    inner class ViewHolder(private val binding: ItemIngredientsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(ingredient: Ingredient) {

            val totalQuantity = BigDecimal(ingredient.quantity)
                .multiply(BigDecimal(quantity))
                .setScale(1, RoundingMode.HALF_UP)
                .stripTrailingZeros()
                .toPlainString()

            binding.apply {
                tvIngredientDescription.text = ingredient.description
                tvIngredientQuantity.text ="$totalQuantity ${ingredient.unitOfMeasure}"
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

    fun updateIngredients(progress: Int) {
        quantity = progress
        notifyDataSetChanged()
    }
}