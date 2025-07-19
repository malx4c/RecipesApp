package com.malx4c.recipesapp.ui.recipes.recipeList

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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

    fun update( _dataSet: List<Recipe>) {
        dataSet = _dataSet
    }

    inner class ViewHolder(private val binding: ItemRecipesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: Recipe) {

            binding.tvTitleRecipes.text = recipe.title

            val drawable = try {
                Drawable.createFromStream(recipe.imageUrl.let { this.itemView.context.assets.open(it) }, null)
            } catch (e: Exception) {
                Log.e("!!! image open error", recipe.imageUrl, e)
                null
            }

            binding.ivRecipes.setImageDrawable(drawable)

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