package com.malx4c.recipesapp.ui.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.malx4c.recipesapp.API_IMAGE_SOURCE
import com.malx4c.recipesapp.API_URL
import com.malx4c.recipesapp.R
import com.malx4c.recipesapp.databinding.ItemCategoryBinding
import com.malx4c.recipesapp.model.Category

class CategoriesListAdapter(private var dataSet: List<Category>) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(categoryId: Int)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    fun update(_dataSet: List<Category?>?) {
        dataSet = _dataSet?.filterNotNull() ?: emptyList()
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category) {
            binding.tvTitleCategories.text = category.title
            binding.tvDescriptionCategories.text = category.description

            Glide.with(this.itemView.context)
                .load("$API_URL$API_IMAGE_SOURCE${category.imageUrl}")
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .into(binding.ivCategories)

            itemView.setOnClickListener { itemClickListener?.onItemClick(category.id) }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {

        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = ItemCategoryBinding.inflate(inflater, viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.bind(dataSet[position])
    }

    override fun getItemCount() = dataSet.size
}