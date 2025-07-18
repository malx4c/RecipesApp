package com.malx4c.recipesapp.ui.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import com.malx4c.recipesapp.ARG_CATEGORY_ID
import com.malx4c.recipesapp.ARG_CATEGORY_IMAGE_URL
import com.malx4c.recipesapp.ARG_CATEGORY_NAME
import com.malx4c.recipesapp.R
import com.malx4c.recipesapp.databinding.FragmentListCategoriesBinding
import com.malx4c.recipesapp.ui.recipes.recipeList.RecipesListFragment

class CategoriesListFragment : Fragment() {
    private val categoriesListViewModel: CategoriesListViewModel by viewModels()

    private var _binding: FragmentListCategoriesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("FragmentListCategoriesBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentListCategoriesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
    }

    private fun initRecycler() {

        val categoryAdapter = categoriesListViewModel.categoriesListViewState.value?.let {
            CategoriesListAdapter(
                it.categories)
        }
        binding.rvCategories.adapter = categoryAdapter

        categoryAdapter?.setOnItemClickListener(object : CategoriesListAdapter.OnItemClickListener {
            override fun onItemClick(categoryId: Int) {
                openRecipesByCategoryId(categoryId)
            }
        })
    }

    private fun openRecipesByCategoryId(categoryId: Int) {
        val category = categoriesListViewModel.getCategoryById(categoryId)
        val bundle = Bundle().apply {
            category?.let {
                putInt(ARG_CATEGORY_ID, it.id)
                putString(ARG_CATEGORY_NAME, it.title)
                putString(ARG_CATEGORY_IMAGE_URL, it.imageUrl)
            }
        }

        parentFragmentManager.commit{
            replace<RecipesListFragment>(R.id.mainContainer, args = bundle)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}