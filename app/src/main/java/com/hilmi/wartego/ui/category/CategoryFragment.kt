package com.hilmi.wartego.ui.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.hilmi.wartego.R
import com.hilmi.wartego.adapters.FoodListAdapter
import com.hilmi.wartego.databinding.FragmentCategoryBinding
import com.hilmi.wartego.model.product.Product
import com.hilmi.wartego.model.response.Response
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class CategoryFragment : Fragment() {

    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CategoryViewModel by viewModels()
    private val args: CategoryFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getFoods(args.id)
        observerCategoryData()
    }

    private fun showLoadingListCategory(isLoad: Boolean) {
        binding.rvPopular.isVisible = !isLoad
        binding.shimmerListFood.apply {
            if (isLoad) startShimmer() else stopShimmer()
            isVisible = isLoad
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun observerCategoryData() {
        val foodAdapter = FoodListAdapter {
            findNavController().navigate(
                CategoryFragmentDirections.actionCategoryFragmentToDetailFragment(
                    it.id
                )
            )
        }

        binding.rvPopular.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = foodAdapter
        }

        viewModel.foods.onEach {
            when (it) {
                is Response.Error -> {
                    showLoadingListCategory(false)
                }

                Response.Loading -> {
                    showLoadingListCategory(true)
                }

                is Response.Success -> {
                    showLoadingListCategory(false)
                    foodAdapter.submitData(it.data as ArrayList<Product>)
                }
            }
        }.launchIn(lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}