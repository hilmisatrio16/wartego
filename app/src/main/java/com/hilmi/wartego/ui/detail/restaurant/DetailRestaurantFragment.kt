package com.hilmi.wartego.ui.detail.restaurant

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.hilmi.wartego.R
import com.hilmi.wartego.adapters.FoodListAdapter
import com.hilmi.wartego.databinding.FragmentDetailRestaurantBinding
import com.hilmi.wartego.model.product.Product
import com.hilmi.wartego.model.product.Restaurant
import com.hilmi.wartego.model.response.Response
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class DetailRestaurantFragment : Fragment() {

    private var _binding: FragmentDetailRestaurantBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailRestaurantViewModel by viewModels()
    private val args: DetailRestaurantFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailRestaurantBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getDetailRestaurant(args.id)
        viewModel.getFoodsByRestaurant(args.id, 1)
        observerDataRestaurant()
        observerDataFood()

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun observerDataFood() {
        viewModel.foodsByRestaurant.onEach {
            when (it) {
                is Response.Error -> {}
                Response.Loading -> {}
                is Response.Success -> {
                    showRecycleView(it.data)
                }
            }
        }.launchIn(lifecycleScope)
    }

    private fun showRecycleView(data: List<Product>) {
        val foodAdapter = FoodListAdapter { food ->
            findNavController().navigate(
                DetailRestaurantFragmentDirections.actionDetailRestaurantFragmentToDetailFragment(
                    food.id
                )
            )
        }

        binding.rvRelatedFood.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = foodAdapter
        }

        foodAdapter.submitData(data as ArrayList<Product>)
    }

    private fun observerDataRestaurant() {
        viewModel.detailRestaurant.onEach {
            when (it) {
                is Response.Error -> {}
                Response.Loading -> {}
                is Response.Success -> {
                    putData(it.data)
                }
            }
        }.launchIn(lifecycleScope)
    }

    private fun putData(data: Restaurant) {
        with(binding) {
            tvRestaurant.text = data.nameRestaurant
            tvRestaurantDesc.text = data.desc
            tvStars.text = data.pointReview
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}