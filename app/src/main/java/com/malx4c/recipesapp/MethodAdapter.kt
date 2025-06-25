package com.malx4c.recipesapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malx4c.recipesapp.databinding.ItemMethodBinding
import com.malx4c.recipesapp.entities.Recipe

class MethodAdapter(private val recipe: Recipe) :
    RecyclerView.Adapter<MethodAdapter.ViewHolder>() {

    private val dataSet: List<String> = recipe.method

    inner class ViewHolder(private val binding: ItemMethodBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(method: String, indexMethod: Int) {

            binding.tvMethod.text = "${indexMethod + 1}. $method"
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {

        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = ItemMethodBinding.inflate(inflater, viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.bind(dataSet[position], position)
    }

    override fun getItemCount() = dataSet.size
}