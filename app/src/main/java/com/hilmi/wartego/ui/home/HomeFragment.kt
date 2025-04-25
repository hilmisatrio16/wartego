package com.hilmi.wartego.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hilmi.wartego.adapters.CategoryListAdapter
import com.hilmi.wartego.adapters.RestaurantListAdapter
import com.hilmi.wartego.adapters.SearchListAdapter
import com.hilmi.wartego.databinding.FragmentHomeBinding
import com.hilmi.wartego.model.product.Category
import com.hilmi.wartego.model.product.Product
import com.hilmi.wartego.model.product.Restaurant
import com.hilmi.wartego.model.response.Response
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnMenu.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToProfileFragment())
        }

        binding.btnCart.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCartFragment())
        }

        binding.searchBar.setOnClickListener {
            binding.svFood.show()
        }

        observerDataCategory()
        observerDataRestaurants()
        searchFood()

    }

    private fun searchFood() {
        binding.svFood.editText.setOnEditorActionListener { textView, _, _ ->
            viewModel.searchFood(textView.text.toString())
            Log.d("HASIL SEARCH", textView.text.toString())
            true
        }

        val searchAdapter = SearchListAdapter {

        }
        binding.rvSearchView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = searchAdapter
        }

        viewModel.listFood.onEach {
            when (it) {
                is Response.Error -> {}
                Response.Loading -> {

                }

                is Response.Success -> {
                    searchAdapter.submitData(it.data as ArrayList<Product>)
                }
            }
        }.launchIn(lifecycleScope)
    }

    private fun showLoadingListRestaurant(isLoad: Boolean) {
        binding.rvRestaurants.isVisible = !isLoad
        binding.shimmerListRestaurant.apply {
            if (isLoad) startShimmer() else stopShimmer()
            isVisible = isLoad
        }
    }

    private fun showLoadingListCategory(isLoad: Boolean) {
        binding.rvCategories.isVisible = !isLoad
        binding.shimmerListCategory.apply {
            if (isLoad) startShimmer() else stopShimmer()
            isVisible = isLoad
        }
    }

    private fun observerDataCategory() {
        val adapterCategory = CategoryListAdapter {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToCategoryFragment(
                    it.id
                )
            )
        }

        binding.rvCategories.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = adapterCategory
        }

        viewModel.category.onEach {
            when (it) {
                is Response.Error -> {
                    showLoadingListCategory(false)
                }

                Response.Loading -> {
                    showLoadingListCategory(true)
                }

                is Response.Success -> {
                    showLoadingListCategory(false)
                    adapterCategory.submitData(it.data as ArrayList<Category>)
                }
            }
        }.launchIn(lifecycleScope)
    }

    private fun observerDataRestaurants() {
        val adapterRestaurant = RestaurantListAdapter {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToDetailRestaurantFragment(
                    it.id
                )
            )
        }
        binding.rvRestaurants.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = adapterRestaurant
        }

        viewModel.restaurant.onEach {
            when (it) {
                is Response.Error -> {
                    showLoadingListRestaurant(false)
                }

                Response.Loading -> {
                    showLoadingListRestaurant(true)
                }

                is Response.Success -> {
                    showLoadingListRestaurant(false)
                    adapterRestaurant.submitData(it.data as ArrayList<Restaurant>)
                    Log.d("DEBUG RESTAURANT 2", it.toString())
                }
            }
        }.launchIn(lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}