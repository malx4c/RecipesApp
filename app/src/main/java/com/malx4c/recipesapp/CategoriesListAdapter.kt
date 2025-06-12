package com.malx4c.recipesapp

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malx4c.recipesapp.databinding.ItemCategoryBinding
import com.malx4c.recipesapp.entities.Category


class CategoriesListAdapter(private val dataSet: List<Category>) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val image = binding.ivCategories
        private val textTitle = binding.tvTitleCategories
        private val textDescription = binding.tvDescriptionCategories

        fun bind(category: Category) {
            textTitle.text = category.title
            textDescription.text = category.description

            val drawable = try {
                val inputStream = this.itemView.context.assets.open(category.imageUrl)
                Drawable.createFromStream(inputStream, null)
            } catch (e: Exception) {
                Log.e("fileErr",category.imageUrl, e)
                null
            }

            image.setImageDrawable(drawable)

        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {

        val inflater = LayoutInflater.from (viewGroup.context)
        val binding = ItemCategoryBinding.inflate(inflater, viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.bind(dataSet[position])
    }

    override fun getItemCount() = dataSet.size
}