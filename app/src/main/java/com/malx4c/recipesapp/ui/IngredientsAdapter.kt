package com.malx4c.recipesapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malx4c.recipesapp.databinding.ItemIngredientsBinding
import com.malx4c.recipesapp.model.Ingredient
import java.math.BigDecimal
import java.math.RoundingMode

class IngredientsAdapter() :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    private var dataSet: List<Ingredient> = emptyList()
    private var quantity: Int = 1

    inner class ViewHolder(private val binding: ItemIngredientsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(ingredient: Ingredient) {
            var totalQuantity = ingredient.quantity

            if (isNumeric(ingredient.quantity)) {
                totalQuantity = BigDecimal(ingredient.quantity)
                    .multiply(BigDecimal(quantity))
                    .setScale(1, RoundingMode.HALF_UP)
                    .stripTrailingZeros()
                    .toPlainString()
            }

            binding.apply {
                tvIngredientDescription.text = ingredient.description
                tvIngredientQuantity.text = "$totalQuantity ${ingredient.unitOfMeasure}"
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

    fun update(_dataSet: List<Ingredient>) {
        dataSet = _dataSet
        notifyDataSetChanged()
    }


    private fun isNumeric(value: String?): Boolean =
        value?.toBigDecimalOrNull() != null
}