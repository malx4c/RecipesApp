package com.malx4c.recipesapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.malx4c.recipesapp.databinding.FragmentListRecipesBinding

class RecipesListFragment : Fragment() {

    private var categoryId: Int? = null
    private var categoryName: String? = null
    private var categoryImageUrl: String? = null

    private var _binding: FragmentListRecipesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("FragmentListRecipesBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentListRecipesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            categoryId = it.getInt("ARG_CATEGORY_ID")
            categoryName = it.getString("ARG_CATEGORY_NAME")
            categoryImageUrl = it.getString("ARG_CATEGORY_IMAGE_URL")
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}