package com.malx4c.recipesapp

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malx4c.recipesapp.databinding.ItemRecipesBinding
import com.malx4c.recipesapp.entities.Recipe

class RecipesListAdapter(private val dataSet: List<Recipe>) :
    RecyclerView.Adapter<RecipesListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(recipesId: Int)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    inner class ViewHolder(private val binding: ItemRecipesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: Recipe) {
            binding.tvTitleRecipes.text = recipe.title

            val drawable = try {
                val inputStream = this.itemView.context.assets.open(recipe.imageUrl)
                Drawable.createFromStream(inputStream, null)
            } catch (e: Exception) {
                Log.e("fileErr", recipe.imageUrl, e)
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