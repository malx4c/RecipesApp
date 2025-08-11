package com.malx4c.recipesapp.ui.recipes.recipeList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.malx4c.recipesapp.API_IMAGE_SOURCE
import com.malx4c.recipesapp.API_URL
import com.malx4c.recipesapp.R
import com.malx4c.recipesapp.databinding.ItemRecipesBinding
import com.malx4c.recipesapp.model.Recipe

class RecipesListAdapter(private var dataSet: List<Recipe>) :
    RecyclerView.Adapter<RecipesListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(recipesId: Int)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    fun update(_dataSet: List<Recipe?>?) {
        dataSet = _dataSet?.filterNotNull() ?: emptyList()
    }

    inner class ViewHolder(private val binding: ItemRecipesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: Recipe) {

            binding.tvTitleRecipes.text = recipe.title

            Glide.with(this.itemView.context)
                .load("$API_URL$API_IMAGE_SOURCE${recipe.imageUrl}")
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .into(binding.ivRecipes)

            itemView.setOnClickListener { itemClickListener?.onItemClick(recipe.id) }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {

        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = ItemRecipesBinding.inflate(inflater, viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.bind(dataSet[position])
    }

    override fun getItemCount() = dataSet.size
}