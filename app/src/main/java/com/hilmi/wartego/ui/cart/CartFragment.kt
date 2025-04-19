package com.hilmi.wartego.ui.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.hilmi.wartego.adapters.CartListAdapter
import com.hilmi.wartego.databinding.FragmentCartBinding
import com.hilmi.wartego.model.profile.Cart
import com.hilmi.wartego.model.response.Response
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class CartFragment : Fragment() {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CartViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observerDataCart()
    }

    private fun observerDataCart() {
        viewModel.cart.onEach {
            when (it) {
                is Response.Error -> {

                }

                Response.Loading -> {

                }

                is Response.Success -> {
                    showRecycleView(it.data)
                }
            }
        }.launchIn(lifecycleScope)
    }

    private fun showRecycleView(data: List<Cart>) {
        val cartAdapter = CartListAdapter()

        binding.rvCartItems.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = cartAdapter
        }

        cartAdapter.submitData(data as ArrayList<Cart>)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}