package com.hilmi.wartego.ui.detail.food

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.hilmi.wartego.R
import com.hilmi.wartego.adapters.FoodListAdapter
import com.hilmi.wartego.databinding.FragmentDetailBinding
import com.hilmi.wartego.model.product.Product
import com.hilmi.wartego.model.profile.Cart
import com.hilmi.wartego.model.response.Response
import com.hilmi.wartego.ui.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailViewModel by viewModels()
    private val args: DetailFragmentArgs by navArgs()
    private lateinit var product: Product
    private var price: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getFood(args.id)
        observerDataProduct()
        observerDataRelatedProduct()
        observerQuantity()
        observerCart()

        binding.btnPlus.setOnClickListener {
            viewModel.addQuantity()
        }

        binding.btnMin.setOnClickListener {
            viewModel.minQuantity()
        }

        binding.btnAdd.setOnClickListener {
            val cart = Cart(
                idProduct = product.id,
                nameProduct = product.nameProduct,
                imageUrl = product.imageUrl,
                price = product.price,
                quantity = viewModel.quantity.value
            )
            viewModel.addCart(
                cart
            )
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun observerCart() {
        viewModel.addCartSuccess.filterNotNull().onEach {
            when (it) {
                is Response.Error -> {}
                is Response.Loading -> {}
                is Response.Success -> {

                }
            }
        }.launchIn(lifecycleScope)
    }

    @SuppressLint("SetTextI18n")
    private fun observerQuantity() {
        viewModel.quantity.onEach {
            binding.tvQuantity.text = it.toString()

            price?.let { priceFood ->
                binding.tvTotalPrice.text = "${it * priceFood.toInt()}"
            }


        }.launchIn(lifecycleScope)
    }

    private fun observerDataRelatedProduct() {
        viewModel.foods.onEach {
            when (it) {
                is Response.Error -> {
                    showLoading(false)
                }

                Response.Loading -> {
                    showLoading(true)
                }

                is Response.Success -> {
                    showLoading(false)
                    putDataInRecycleView(it.data.filter { item ->
                        item.id != args.id
                    })
                }
            }
        }.launchIn(lifecycleScope)
    }

    private fun putDataInRecycleView(data: List<Product>) {
        val foodAdapter = FoodListAdapter {

        }
        binding.rvRelatedFood.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = foodAdapter
        }

        foodAdapter.submitData(data as ArrayList<Product>)
    }

    private fun observerDataProduct() {
        viewModel.food.onEach {
            when (it) {
                is Response.Error -> {}
                Response.Loading -> {}
                is Response.Success -> {
                    putData(it.data)
                    product = it.data
                }
            }
        }.launchIn(lifecycleScope)
    }

    private fun putData(data: Product) {
        viewModel.getFoods(data.idCategory)
        with(binding) {
            tvFood.text = data.nameProduct
            tvTotalPrice.text = data.price
            price = data.price
//            tvDetail.text = data.
        }
    }

    private fun showLoading(isLoad: Boolean) {
        binding.layoutConstDetail.isVisible = !isLoad
        binding.tvTotalPrice.isVisible = !isLoad
        binding.shimmerDetailFood.apply {
            if (isLoad) startShimmer() else stopShimmer()
            isVisible = isLoad
        }

        binding.shimerTotalPrice.apply {
            if (isLoad) startShimmer() else stopShimmer()
            isVisible = isLoad
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}