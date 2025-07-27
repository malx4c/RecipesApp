package com.malx4c.recipesapp.ui.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.malx4c.recipesapp.ARG_CATEGORY_ID
import com.malx4c.recipesapp.ARG_CATEGORY_IMAGE_URL
import com.malx4c.recipesapp.ARG_CATEGORY_NAME
import com.malx4c.recipesapp.R
import com.malx4c.recipesapp.databinding.FragmentListCategoriesBinding

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
        val categoryAdapter = CategoriesListAdapter(emptyList())
        binding.rvCategories.adapter = categoryAdapter

        val categoryObserver = Observer<CategoriesListViewModel.CategoriesListUiState> {
            categoryAdapter.update(it.categories)
        }

        categoriesListViewModel.categoriesListViewState.observe(
            viewLifecycleOwner,
            categoryObserver
        )

        categoryAdapter.setOnItemClickListener(object :
            CategoriesListAdapter.OnItemClickListener {
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

        findNavController().navigate(R.id.recipesListFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}